package com.loopers.application.common.event;

import lombok.Getter;

import java.util.UUID;
@Getter
public class CacheEvent {
    private String eventId;
    private Long productId;

    public static CacheEvent create(Long productId) {
        return new CacheEvent(productId);
    }

    public CacheEvent(Long productId) {
        this.eventId = System.currentTimeMillis() + "-" + UUID.randomUUID();
        this.productId = productId;
    }
}
