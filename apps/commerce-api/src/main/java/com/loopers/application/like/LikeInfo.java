package com.loopers.application.like;

import com.loopers.application.product.ProductInfo;
import com.loopers.domain.product.ProductData;

import java.util.List;

public class LikeInfo {

    public record ProductListInfo(List<ProductData> products) {
        public static ProductInfo.ProductListInfo from(List<ProductData> products) {
            return new ProductInfo.ProductListInfo(products);
        }
    }
}
