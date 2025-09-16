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

    @Override
    public void carryOverRanking() {

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        String todayKey = "ranking:all:" + today.format(DateTimeFormatter.BASIC_ISO_DATE);
        String tomorrowKey = "ranking:all:" + tomorrow.format(DateTimeFormatter.BASIC_ISO_DATE);

        rankingRepository.carryOver(todayKey, tomorrowKey, 0.1);
    }
}
