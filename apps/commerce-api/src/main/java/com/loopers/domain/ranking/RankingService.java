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

    public String getProductRank(Long id) {
        List<RankingDto> ranKingList = rankingRepository.getTodayRanKingList();

        for (int i = 0; i < ranKingList.size(); i++) {
            if (ranKingList.get(i).getProductId().equals(id)) {
                return String.valueOf(i + 1);
            }
        }
        return "-";
    }
}
