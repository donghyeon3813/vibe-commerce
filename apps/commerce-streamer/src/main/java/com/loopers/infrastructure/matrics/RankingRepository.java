package com.loopers.infrastructure.matrics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class RankingRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public void updateRankingScores(String key, Map<Long, Double> rankingScores) {
        boolean isNewKey = Boolean.FALSE.equals(redisTemplate.hasKey(key));

        for (Map.Entry<Long, Double> entry : rankingScores.entrySet()) {
            redisTemplate.opsForZSet().incrementScore(key, entry.getKey().toString(), entry.getValue());
        }

        if (isNewKey) {
            redisTemplate.expire(key, Duration.ofDays(2));
        }
    }
}
