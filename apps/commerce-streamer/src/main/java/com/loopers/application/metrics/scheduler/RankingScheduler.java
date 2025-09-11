package com.loopers.application.metrics.scheduler;

import com.loopers.domain.metrics.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class RankingScheduler {
    private final RankingService rankingService;
    @Scheduled(cron = "0 50 23 * * *")
    public void carryOverRanking() {
        rankingService.carryOverRanking();
    }

}
