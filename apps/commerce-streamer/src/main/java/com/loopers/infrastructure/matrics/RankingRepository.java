package com.loopers.infrastructure.matrics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.Set;

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

    public void carryOver(String todayKey, String tomorrowKey, double decayFactor) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(todayKey))) {
            Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet().rangeWithScores(todayKey, 0, -1);

            for (ZSetOperations.TypedTuple<String> tuple : tuples) {
                double newScore = tuple.getScore() * decayFactor;
                redisTemplate.opsForZSet().add(tomorrowKey, tuple.getValue(), newScore);
            }
            redisTemplate.expire(tomorrowKey, Duration.ofDays(2));
            log.info("Carry over tomorrow key: " + tomorrowKey);
        }
    }
}
