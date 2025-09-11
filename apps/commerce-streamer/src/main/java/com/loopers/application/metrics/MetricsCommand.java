package com.loopers.application.metrics;

import lombok.Getter;

public class MetricsCommand {
    @Getter
    public static class Adjust {
        private String eventId;
        private Long productId;
        private EventType eventType;
        private int count;
        private String consumer;
        public static Adjust of(String eventId, Long productId, EventType eventType, int count, String consumer) {
            return new Adjust(eventId, productId, eventType, count, consumer);
        }

        public Adjust(String eventId, Long productId, EventType eventType, int count, String consumer) {
            this.eventId = eventId;
            this.productId = productId;
            this.eventType = eventType;
            this.count = count;
            this.consumer = consumer;
        }
        public enum EventType {
            LIKE_EVENT,
            UNLIKE_EVENT,
            PRODUCT_SOLD_EVENT,
            PRODUCT_VIEW_EVENT;
            public static EventType getEventType(String eventType) {
                return EventType.valueOf(eventType.toUpperCase());
            }
        }
    }
}
