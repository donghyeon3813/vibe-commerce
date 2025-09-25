package com.loopers.domain.ranking;

import com.loopers.application.ranking.RankingCommand;
import com.loopers.infrastructure.ranking.RankingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<RankingDto> getWeeklyRanKingList(RankingCommand.Get command) {
         rankingRepository.getWeeklyRankingList(command);
        LocalDate date = LocalDate.parse(command.getDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
        int size = command.getSize();
        int page = command.getPage() - 1;
        Pageable pageable = PageRequest.of(page, size);
        List<MvProductRankWeekly> weeklyRanKingList = rankingRepository.getWeeklyRanKingList(date, pageable);

        return weeklyRanKingList.stream()
                .map(r -> new RankingDto(r.getProductId(), r.getScore()))
                .collect(Collectors.toList());
    }

    public List<RankingDto> getMontlhyRanKingList(RankingCommand.Get command) {
        rankingRepository.getWeeklyRankingList(command);
        LocalDate date = LocalDate.parse(command.getDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
        int size = command.getSize();
        int page = command.getPage() - 1;
        Pageable pageable = PageRequest.of(page, size);
        List<MvProductRankMonthly> weeklyRanKingList = rankingRepository.getMonthlyRanKingList(date, pageable);

        return weeklyRanKingList.stream()
                .map(r -> new RankingDto(r.getProductId(), r.getScore()))
                .collect(Collectors.toList());
    }
}
