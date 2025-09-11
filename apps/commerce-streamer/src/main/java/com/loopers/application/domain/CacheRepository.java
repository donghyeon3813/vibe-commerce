package com.loopers.application.domain;

import java.util.Map;

public interface CacheRepository {
    void EvictProducts();

    void EvictProduct(Long productId);
}
