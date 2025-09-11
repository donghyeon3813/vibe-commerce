package com.loopers.infrastructure.ranking;

import com.loopers.application.ranking.RankingCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RankingCacheRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public List<RankingDto> getRanKingList(RankingCommand.Get command) {
        String key = "ranking:all:"+command.getDate();
        int start = (command.getPage() - 1) * command.getSize();
        int end = start + command.getSize() - 1;
        Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
        if (tuples.isEmpty()) {
            return Collections.emptyList();
        }
        return tuples.stream()
                .map(tuple -> new RankingDto(Long.parseLong(tuple.getValue()), tuple.getScore()))
                .collect(Collectors.toList());
    }

    public List<RankingDto> getTodayRanKingList() {
        LocalDate today = LocalDate.now();
        String dateKey = today.format(DateTimeFormatter.BASIC_ISO_DATE);
        String key = "ranking:all:"+dateKey;

        Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, -1);
        if (tuples.isEmpty()) {
            return Collections.emptyList();
        }
        return tuples.stream()
                .map(tuple -> new RankingDto(Long.parseLong(tuple.getValue()), tuple.getScore()))
                .collect(Collectors.toList());
    }
}
