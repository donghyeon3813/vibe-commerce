package com.loopers.domain.ranking;

import com.loopers.application.ranking.RankingCommand;
import com.loopers.infrastructure.ranking.RankingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RankingService {
    private final RankingRepository rankingRepository;

    public List<RankingDto> getRanKingList(RankingCommand.Get command) {
        return rankingRepository.getRanKingList(command);
    }
}
