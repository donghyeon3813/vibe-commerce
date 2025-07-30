package com.loopers.application.product;

import com.loopers.domain.product.ProductData;

public record ProductInfo() {
    record ProductDetailInfo(String productName, int amount, int quantity, String brandName, int likeCount) {
        public static ProductDetailInfo from(ProductData productData) {
            return new ProductDetailInfo(productData.productName(), productData.amount(), productData.quantity(), productData.brandName(), productData.likeCount());
        }

    }

//    record ProductList(List<ProductDetailInfo> products) {
//
//    }

}
