package com.loopers.infrastructure.ranking;

import com.loopers.application.ranking.RankingCommand;
import com.loopers.domain.ranking.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RankingRepositoryImpl implements RankingRepository {
    private final RankingCacheRepository repository;


    @Override
    public List<RankingDto> getRanKingList(RankingCommand.Get command) {

        return repository.getRanKingList(command);
    }

    @Override
    public List<RankingDto> getTodayRanKingList() {
        return repository.getTodayRanKingList();
    }
}
