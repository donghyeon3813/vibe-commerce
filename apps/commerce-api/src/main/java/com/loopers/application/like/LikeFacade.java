package com.loopers.application.like;

import com.loopers.application.common.event.CacheEvent;
import com.loopers.application.common.event.MetricsEvent;
import com.loopers.application.product.ProductInfo;
import com.loopers.application.productLike.listener.ProductLikeEvent;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductData;
import com.loopers.domain.product.ProductDetailComposer;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LikeFacade {

    private final LikeService likeService;
    private final UserService userService;
    private final ProductService productService;
    private final BrandService brandService;
    private final ProductDetailComposer productDetailComposer;
    private final ApplicationEventPublisher eventPublisher;
    private final KafkaTemplate<Object, Object> kafkaTemplate;


    @Transactional
    public void register(LikeCommand.RegisterDto registerDto) {

        UserModel user = userService.getUser(registerDto.userId());
        if (user == null) {
            throw new CoreException(ErrorType.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
        Product product = productService.getProductInfo(registerDto.productId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));

        likeService.resist(user.getId(), registerDto.productId());
        eventPublisher.publishEvent(ProductLikeEvent.LikeIncrementEvent.of(product.getId(), product.getBrandUid()));
        kafkaTemplate.send("catalog-events",
                product.getId().toString(),
                MetricsEvent.of(product.getId(), MetricsEvent.EventType.LIKE_EVENT, 1));
        kafkaTemplate.send("cache-evicts-events",
                product.getId().toString(),
                CacheEvent.create(product.getId()));
    }

    @Transactional
    @Retryable
    public void delete(LikeCommand.DeleteDto deleteDto) {
        UserModel user = userService.getUser(deleteDto.userId());
        if (user == null) {
            throw new CoreException(ErrorType.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }

        likeService.unLike(user.getId(), deleteDto.productId());
        eventPublisher.publishEvent(ProductLikeEvent.LikeDecrementEvent.of(deleteDto.productId()));
        kafkaTemplate.send("catalog-events",
                deleteDto.productId().toString(),
                MetricsEvent.of(deleteDto.productId(), MetricsEvent.EventType.UNLIKE_EVENT, -1));
        kafkaTemplate.send("cache-evicts-events",
                deleteDto.productId().toString(),
                CacheEvent.create(deleteDto.productId()));
    }

    @Transactional(readOnly = true)
    public ProductInfo.ProductListInfo getProducts(LikeCommand.GetProduct request) {
        UserModel user = userService.getUser(request.userId());
        if (user == null) {
            throw new CoreException(ErrorType.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
        List<Like> likes = likeService.findByUserUid(user.getId());
        Set<Long> productUids = likes.stream()
                .map(Like::getProductUid)
                .collect(Collectors.toSet());

        List<Product> products = productService.getProductsByProducUids(productUids);
        List<Long> brandUids = products.stream()
                .map(Product::getBrandUid)
                .toList();

        List<Brand> brands = brandService.getBrandsByBrandUids(brandUids);

        if (products.isEmpty() || brands.isEmpty()) {
            return LikeInfo.ProductListInfo.from(Collections.emptyList());
        }

        List<ProductData> productDatas = productDetailComposer.composeList(products, brands, likes);
        return LikeInfo.ProductListInfo.from(productDatas);
    }
}
