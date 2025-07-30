package com.loopers.domain.product;

import com.loopers.domain.brand.Brand;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;


public record ProductData(String productName, int amount, int quantity, String brandName, int likeCount) {
    public static ProductData from(Product product, Brand brand, int likeCount) {
        if (product == null || brand == null) {
            throw new CoreException(ErrorType.NOT_FOUND, "상품 또는 브랜드 정보가 누락되었습니다.");
        }
        return new ProductData(product.getName(), product.getAmount(), product.getQuantity(), brand.getName(), likeCount);
    }
}
