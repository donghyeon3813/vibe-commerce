package com.loopers.domain.ranking;

import com.loopers.application.ranking.RankingCommand;
import com.loopers.infrastructure.ranking.RankingDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface RankingRepository {
    List<RankingDto> getRanKingList(RankingCommand.Get command);

    List<RankingDto> getTodayRanKingList();

    List<MvProductRankWeekly> getWeeklyRankingList(RankingCommand.Get command);

    List<MvProductRankWeekly> getWeeklyRanKingList(LocalDate date, Pageable pageable);

    List<MvProductRankMonthly> getMonthlyRanKingList(LocalDate date, Pageable pageable);
}
