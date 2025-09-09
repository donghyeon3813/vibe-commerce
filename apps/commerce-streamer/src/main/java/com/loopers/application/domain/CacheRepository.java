package com.loopers.application.domain;

public interface CacheRepository {
    void EvictProducts();

    void EvictProduct(Long productId);
}
