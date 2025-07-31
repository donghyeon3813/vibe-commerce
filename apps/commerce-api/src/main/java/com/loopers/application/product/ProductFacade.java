package com.loopers.application.product;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductData;
import com.loopers.domain.product.ProductDetailComposer;
import com.loopers.domain.product.ProductService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductFacade {
    private final ProductService productService;
    private final BrandService brandService;
    private final LikeService likeService;
    private final ProductDetailComposer productDetailComposer;


    public ProductInfo.ProductListInfo getProductList(ProductCommand.ListInfoRequest productCommand) {
        Sort sort = Sort.by(productCommand.sort.getDirection(), productCommand.sort.getField());

        Pageable pageable = PageRequest.of(productCommand.getPage(), productCommand.getSize(), sort);

        List<ProductData> productList = productService.getProductList(productCommand.brandId, pageable);


        return ProductInfo.ProductListInfo.from(productList);
    }

    public ProductInfo.ProductDetailInfo getProductDetailInfo(ProductCommand.DetailInfoRequest getInfo) {
        Optional<Product> productOptional = productService.getProductInfo(getInfo.productId());

        if (productOptional.isEmpty()) {
            throw new CoreException(ErrorType.NOT_FOUND, "상품 정보가 존재하지 않습니다.");
        }

        Product product = productOptional.get();

        Optional<Brand> Brand = brandService.getBrandInfo(product.getBrandUid());
        if (Brand.isEmpty()) {
            throw new CoreException(ErrorType.NOT_FOUND, "Brand 정보가 존재하지 않습니다.");
        }
        Brand brand = Brand.get();
        int count = likeService.getCountByProductUid(product.getId());

        return ProductInfo.ProductDetailInfo.from(productDetailComposer.compose(product, brand, count));
    }
}
