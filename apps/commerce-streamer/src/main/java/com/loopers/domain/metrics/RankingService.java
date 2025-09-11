package com.loopers.domain.metrics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RankingService {
    private final RankingRepository rankingRepository;
    public void updateRanking(Map<Long, Double> rankingScores) {
        rankingRepository.updateRanking(rankingScores);
    }

    public void carryOverRanking() {
        rankingRepository.carryOverRanking();
    }
}
