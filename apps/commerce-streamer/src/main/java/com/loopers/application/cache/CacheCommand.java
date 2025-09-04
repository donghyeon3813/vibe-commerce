package com.loopers.application.cache;

import lombok.Getter;

public class CacheCommand {
    @Getter
    public static class EvictCache{
        private String eventId;
        private Long productId;

        public static EvictCache create(String eventId, Long productId){
            return new EvictCache(eventId, productId);
        }

        public EvictCache(String eventId, Long productId) {
            this.eventId = eventId;
            this.productId = productId;
        }

    }
}
