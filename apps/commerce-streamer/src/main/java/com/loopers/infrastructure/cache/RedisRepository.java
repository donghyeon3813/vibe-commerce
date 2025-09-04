package com.loopers.infrastructure.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public void evictCache(String key) {
        redisTemplate.delete(key);
        log.info("Evicted cache key: {}", key);
    }

    // 다건 삭제
    public void evictAllAccessPageCache(List<String> productListKet) {
        redisTemplate.delete(productListKet);
        log.info("Evicted all access page cache keys: {}", productListKet);
    }
}
