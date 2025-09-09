package com.loopers.application.common.event;

import lombok.Getter;

import java.util.UUID;

@Getter
public class MetricsEvent {
    private String eventId;
    private Long productId;
    private EventType type;
    private int count;
    public static MetricsEvent of(Long productId, EventType type, int count) {
        return new MetricsEvent(productId, type, count);
    }

    public MetricsEvent(Long productId, EventType type, int count) {
        this.eventId = System.currentTimeMillis() + "-" + UUID.randomUUID();
        this.productId = productId;
        this.type = type;
        this.count = count;
    }
    @Getter
    public enum EventType {
        LIKE_EVENT,
        UNLIKE_EVENT,
        PRODUCT_SOLD_EVENT,
        PRODUCT_VIEW_EVENT,
    }
}
