package com.loopers.infrastructure.matrics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RankingRepositoryImpl implements com.loopers.domain.metrics.RankingRepository {

    private final RankingRepository rankingRepository;

    @Override
    public void updateRanking(Map<Long, Double> rankingScores) {
        LocalDate today = LocalDate.now();
        String dateKey = today.format(DateTimeFormatter.BASIC_ISO_DATE);
        String key = "ranking:all:"+dateKey;
        rankingRepository.updateRankingScores(key, rankingScores);
    }
}
