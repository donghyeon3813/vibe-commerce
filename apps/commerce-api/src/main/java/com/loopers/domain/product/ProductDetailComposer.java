package com.loopers.domain.product;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.like.Like;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProductDetailComposer {
    public ProductData compose(Product product, Brand brand, int count) {
        return ProductData.from(product, brand, count);
    }

    public List<ProductData> composeList(List<Product> products, List<Brand> brands, List<Like> likes) {

        Map<Long, Brand> brandMap = brands.stream()
                .collect(Collectors.toMap(Brand::getId, brand -> brand));

        Map<Long, Long> likeCountMap = likes.stream()
                .collect(Collectors.groupingBy(
                        Like::getProductUid,
                        Collectors.counting()
                ));

        return products.stream()
                .map(product -> {
                    Brand brand = brandMap.get(product.getBrandUid());
                    int likeCount = likeCountMap.getOrDefault(product.getId(), 0L).intValue();
                    return compose(product, brand, likeCount);
                })
                .collect(Collectors.toList());
    }
}
