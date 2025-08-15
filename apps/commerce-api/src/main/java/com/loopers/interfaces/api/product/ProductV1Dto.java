package com.loopers.interfaces.api.product;

import com.loopers.application.product.ProductInfo;
import jakarta.validation.constraints.Pattern;

public class ProductV1Dto {
    public record ProductListRequest(Long brandUid,
                                     int page,
                                     int size,
                                     @Pattern(regexp = "LATEST|PRICE_ASC|LIKE_DESC",
                                             message = "sort 옵션은 LATEST, PRICE_ASC, LIKE_DESC 만 설정이 가능합니다.")
                                     String sort) {
    }
    public record ProductListResponse(ProductInfo.ProductListInfo response) {
        public static ProductListResponse from(ProductInfo.ProductListInfo productInfo) {
            return new ProductListResponse(productInfo);
        }
    }


    public record ProductDetailResponse(ProductInfo.ProductDetailInfo productInfo) {
        public static ProductDetailResponse from(ProductInfo.ProductDetailInfo productInfo) {
            return new ProductDetailResponse(productInfo);
        }
    }
}
