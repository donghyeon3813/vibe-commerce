package com.loopers.application.domain;

import com.loopers.application.cache.CacheCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheService {
    private final CacheRepository cacheRepository;

    public void EvictProductCache(CacheCommand.EvictCache command) {
        cacheRepository.EvictProducts();
        cacheRepository.EvictProduct(command.getProductId());
    }
}
