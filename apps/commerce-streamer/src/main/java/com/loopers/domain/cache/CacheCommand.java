package com.loopers.domain.cache;

import lombok.Getter;

public class CacheCommand {
    @Getter
    public static class EvictCache{
        private String eventId;
        private Long productId;
        private String consumer;

        public static EvictCache create(String eventId, Long productId, String consumer){
            return new EvictCache(eventId, productId, consumer);
        }

        public EvictCache(String eventId, Long productId, String consumer) {
            this.eventId = eventId;
            this.productId = productId;
            this.consumer = consumer;
        }

    }
}
