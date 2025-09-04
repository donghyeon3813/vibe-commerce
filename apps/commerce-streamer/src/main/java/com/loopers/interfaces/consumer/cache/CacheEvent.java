package com.loopers.interfaces.consumer.cache;

import lombok.Getter;

@Getter
public class CacheEvent {
    private String eventId;
    private Long productId;
}
