package com.loopers.infrastructure.ranking;

import com.loopers.application.ranking.RankingCommand;
import com.loopers.domain.ranking.MvProductRankMonthly;
import com.loopers.domain.ranking.MvProductRankWeekly;
import com.loopers.domain.ranking.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RankingRepositoryImpl implements RankingRepository {
    private final RankingCacheRepository repository;
    private final MvProductRankWeeklyJpaRepository mvProductRankWeeklyRepository;
    private final MvProductRankMonthlyJpaRepository mvProductRankMonthlyRepository;



    @Override
    public List<RankingDto> getRanKingList(RankingCommand.Get command) {

        return repository.getRanKingList(command);
    }

    @Override
    public List<RankingDto> getTodayRanKingList() {
        return repository.getTodayRanKingList();
    }

    @Override
    public List<MvProductRankWeekly> getWeeklyRankingList(RankingCommand.Get command) {
        return List.of();
    }

    @Override
    public List<MvProductRankWeekly> getWeeklyRanKingList(LocalDate date, Pageable pageable) {
        return mvProductRankWeeklyRepository.findByAggDateOrderByScoreDesc(date, pageable);

    }

    @Override
    public List<MvProductRankMonthly> getMonthlyRanKingList(LocalDate date, Pageable pageable) {
        return mvProductRankMonthlyRepository.findByAggDateOrderByScoreDesc(date, pageable);
    }

}
