package com.loopers.application.metrics;

import com.loopers.domain.event.EventHandleLog;
import com.loopers.domain.event.EventHandleLogService;
import com.loopers.domain.metrics.ProductMetrics;
import com.loopers.domain.metrics.ProductMetricsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class MetricsFacade {

    private final ProductMetricsService productMetricsService;
    private final EventHandleLogService eventHandleService;

    @Transactional
    public void aggregate(List<MetricsCommand.Adjust> commands) {
        if (commands.isEmpty()) return;

        Set<String> eventIds = commands.stream()
                .map(MetricsCommand.Adjust::getEventId)
                .collect(Collectors.toSet());

        String consumer = commands.get(0).getConsumer(); // 동일 consumer 전제
        List<EventHandleLog> processedLogs =
                eventHandleService.findByEventIdsAndConsumer(eventIds, consumer);

        Set<String> processedEventIds = processedLogs.stream()
                .map(EventHandleLog::getEventId)
                .collect(Collectors.toSet());

        LocalDate today = LocalDate.now();

        for (MetricsCommand.Adjust command : commands) {
            if (processedEventIds.contains(command.getEventId())) {
                continue; // 이미 처리된 이벤트는 skip
            }

            ProductMetrics metrics = productMetricsService
                    .findByProductIdAndMetricsDate(command.getProductId(), today)
                    .orElseGet(() -> productMetricsService.save(command.getProductId()));

            switch (command.getEventType()) {
                case LIKE_EVENT, UNLIKE_EVENT -> metrics.adjustLikeCount(command.getCount());
                case PRODUCT_SOLD_EVENT -> metrics.incrementSaleCount(command.getCount());
                case PRODUCT_VIEW_EVENT -> metrics.incrementViewCount(command.getCount());
            }

            eventHandleService.save(command.getEventId(), command.getConsumer());
        }
    }
}
