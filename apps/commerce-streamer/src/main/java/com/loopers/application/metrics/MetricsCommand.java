package com.loopers.application.metrics;

import lombok.Getter;

public class MetricsCommand {
    @Getter
    public static class Adjust {
        private String eventId;
        private Long productId;
        private EventType eventType;
        private int count;
        public static Adjust of(String eventId, Long productId, EventType eventType, int count) {
            return new Adjust(eventId, productId, eventType, count);
        }

        public Adjust(String eventId, Long productId, EventType eventType, int count) {
            this.eventId = eventId;
            this.productId = productId;
            this.eventType = eventType;
            this.count = count;
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
