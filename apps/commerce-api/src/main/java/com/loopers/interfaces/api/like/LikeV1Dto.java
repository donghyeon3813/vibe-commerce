package com.loopers.interfaces.api.like;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class LikeV1Dto {
    @Getter
    @NoArgsConstructor
    public static class LikeRequest {
        private Long productId;

        public LikeRequest(Long productId) {
            this.productId = productId;
        }
    }
    @Getter
    @NoArgsConstructor
    public static class UnlikeRequest {
        private Long productId;

        public UnlikeRequest(Long productId) {
            this.productId = productId;
        }
    }

    public class LikeResponse {
    }
}
