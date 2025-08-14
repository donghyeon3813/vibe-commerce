package com.loopers.infrastructure.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopers.domain.product.ProductData;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.List;


@Component
@RequiredArgsConstructor
public class ProductRedisRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final Duration DEFAULT_TTL = Duration.ofMinutes(1);
    private static final List<String> accessPageListKey = List.of(
            "search:product:brand:0:page:1:size:20:sort:likeCount:DESC",
            "search:product:brand:0:page:2:size:20:sort:likeCount:DESC",
            "search:product:brand:0:page:3:size:20:sort:likeCount:DESC"
    );


    public List<ProductData> get(String key) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) return Collections.emptyList();
        try {
            return objectMapper.readValue(
                    value,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ProductData.class)
            );
        } catch (JsonProcessingException e) {
            throw new CoreException(ErrorType.INTERNAL_ERROR, "Error parsing product Get data");
        }
    }

    public void set(String key, List<ProductData> value) {
        if (!accessPageListKey.contains(key)) {
            return;
        }
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json, DEFAULT_TTL);
        } catch (JsonProcessingException e) {
            throw new CoreException(ErrorType.INTERNAL_ERROR, "Error parsing product Set data");
        }
    }
}
