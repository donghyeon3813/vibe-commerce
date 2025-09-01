package com.loopers.application.productLike.listener;

import lombok.Getter;
import lombok.ToString;


public class ProductLikeEvent {
    @Getter
    @ToString
    public static class LikeIncrementEvent {
        private Long productId;
        private Long brandId;
        private LikeIncrementEvent(Long id, Long brandId) {
            this.productId = id;
            this.brandId = brandId;
        }
        public static LikeIncrementEvent of(Long id, Long brandId) {
            return new LikeIncrementEvent(id, brandId);
        }
    }
    @Getter
    public static class LikeDecrementEvent {
        private Long productId;
        private LikeDecrementEvent(Long id) {
            this.productId = id;
        }
        public static LikeDecrementEvent of(Long id) {
            return new LikeDecrementEvent(id);
        }
    }

}
