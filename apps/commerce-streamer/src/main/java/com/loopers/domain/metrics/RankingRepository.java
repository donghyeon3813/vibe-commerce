package com.loopers.domain.metrics;

import java.util.Map;

public interface RankingRepository {
    void updateRanking(Map<Long, Double> rankingScores);

    void carryOverRanking();
}
