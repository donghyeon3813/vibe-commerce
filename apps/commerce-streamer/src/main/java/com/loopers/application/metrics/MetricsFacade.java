package com.loopers.application.metrics;

import com.loopers.application.domain.CacheService;
import com.loopers.domain.event.EventHandleLog;
import com.loopers.domain.event.EventHandleLogService;
import com.loopers.domain.metrics.ProductMetrics;
import com.loopers.domain.metrics.ProductMetricsService;
import com.loopers.domain.metrics.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.loopers.application.metrics.MetricsCommand.Adjust.EventType.PRODUCT_SOLD_EVENT;
import static com.loopers.application.metrics.MetricsCommand.Adjust.EventType.PRODUCT_VIEW_EVENT;

@Component
@RequiredArgsConstructor
@Slf4j
public class MetricsFacade {

    private final ProductMetricsService productMetricsService;
    private final EventHandleLogService eventHandleService;
    private final RankingService rankingService;

    @Transactional
    public void aggregate(List<MetricsCommand.Adjust> commands) {
        if (commands.isEmpty()) return;

        Set<String> eventIds = commands.stream()
                .map(MetricsCommand.Adjust::getEventId)
                .collect(Collectors.toSet());

        String consumer = commands.get(0).getConsumer();
        List<EventHandleLog> processedLogs =
                eventHandleService.findByEventIdsAndConsumer(eventIds, consumer);

        Set<String> processedEventIds = processedLogs.stream()
                .map(EventHandleLog::getEventId)
                .collect(Collectors.toSet());

        LocalDate today = LocalDate.now();

        Map<Long, Double> rankingScores = new HashMap<>();

        for (MetricsCommand.Adjust command : commands) {
            if (processedEventIds.contains(command.getEventId())) {
                continue; // 이미 처리된 이벤트는 skip
            }

            ProductMetrics metrics = productMetricsService
                    .findByProductIdAndMetricsDate(command.getProductId(), today)
                    .orElseGet(() -> productMetricsService.save(command.getProductId()));
            double score = 0.0;
            switch (command.getEventType()) {
                case LIKE_EVENT -> {
                    metrics.adjustLikeCount(command.getCount());
                    score = 1 * Weight.LIKE.getWeight();
                }
                case UNLIKE_EVENT -> {
                    metrics.adjustLikeCount(command.getCount());
                    score = -1 * Weight.LIKE.getWeight();
                }
                case PRODUCT_SOLD_EVENT -> {
                    metrics.incrementSaleCount(command.getCount());
                    score = 1 * Weight.SOLD.getWeight();
                }
                case PRODUCT_VIEW_EVENT -> {
                    metrics.incrementViewCount(command.getCount());
                    score = 1 * Weight.VIEW.getWeight();
                }
            }
            rankingScores.put(command.getProductId(), rankingScores.getOrDefault(command.getProductId(), 0.0) + score);

            eventHandleService.save(command.getEventId(), command.getConsumer());
            rankingService.updateRanking(rankingScores);
        }
    }
}
