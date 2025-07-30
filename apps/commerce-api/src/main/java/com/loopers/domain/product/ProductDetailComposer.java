package com.loopers.domain.product;

import com.loopers.domain.brand.Brand;
import org.springframework.stereotype.Component;

@Component
public class ProductDetailComposer {
    public ProductData compose(Product product, Brand brand, int count) {
        return ProductData.from(product, brand, count);
    }
}
