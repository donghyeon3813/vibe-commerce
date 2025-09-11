package com.loopers.domain.ranking;

import com.loopers.application.ranking.RankingCommand;
import com.loopers.infrastructure.ranking.RankingDto;

import java.util.List;

public interface RankingRepository {
    List<RankingDto> getRanKingList(RankingCommand.Get command);

    List<RankingDto> getTodayRanKingList();
}
