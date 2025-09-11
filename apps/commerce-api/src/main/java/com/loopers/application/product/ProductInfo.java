package com.loopers.application.product;

import com.loopers.domain.product.ProductData;

import java.util.List;

public record ProductInfo() {
    public record ProductDetailInfo(String productName, int amount, int quantity, String brandName, long likeCount, String rank) {
        public static ProductDetailInfo from(ProductData productData, String rank) {
            return new ProductDetailInfo(productData.productName(), productData.amount(), productData.quantity(), productData.brandName(), productData.likeCount(), rank);
        }

    }


    public record ProductListInfo(List<ProductData> products) {
        public static ProductListInfo from(List<ProductData> products) {
            return new ProductListInfo(products);
        }
    }
}
