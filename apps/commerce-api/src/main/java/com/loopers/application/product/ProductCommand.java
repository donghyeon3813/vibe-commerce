package com.loopers.application.product;

import lombok.Getter;


public class ProductCommand {
    @Getter
    public static class ListInfoRequest {
        Long brandId;
        Sort sort;
        int page;
        int size;

        public ListInfoRequest(Long brandId, Sort sort, int page, int size) {
            this.brandId = brandId;
            this.sort = sort;
            this.page = page;
            this.size = size;
        }

        public enum Sort {
            LATEST,
            PRICE_ASC,
            LIKE_DESC

        }

        public static ListInfoRequest of(Long brandId, Sort sort, int page, int size) {
            return new ListInfoRequest(brandId, sort, page, size);
        }
    }

    public record DetailInfoRequest(Long productId) {
        public static DetailInfoRequest of(Long productId) {
            return new DetailInfoRequest(productId);
        }

    }
}
