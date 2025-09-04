package com.loopers.infrastructure.cache;

import com.loopers.application.domain.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheRepositoryImpl implements CacheRepository {
    private final RedisRepository redisRepository;
    @Override
    public void EvictProducts() {
        redisRepository.evictAllAccessPageCache(ProductCacheKey.PRODUCT_LIST_KET);

    }

    @Override
    public void EvictProduct(Long productId) {
        String keyToCheck = "search:product:" + productId;
        if (ProductCacheKey.PRODUCT_DETAIL_KEY.contains(keyToCheck)) {
            redisRepository.evictCache(keyToCheck);
        }
    }
}
