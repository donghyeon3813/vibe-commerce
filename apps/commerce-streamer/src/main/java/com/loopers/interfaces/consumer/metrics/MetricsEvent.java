package com.loopers.interfaces.consumer.metrics;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MetricsEvent {

    private String eventId;
    private Long productId;
    private EventType type;
    private int count;

    public enum EventType {
        LIKE_EVENT,
        UNLIKE_EVENT,
        PRODUCT_SOLD_EVENT,
        PRODUCT_VIEW_EVENT
    }
}
