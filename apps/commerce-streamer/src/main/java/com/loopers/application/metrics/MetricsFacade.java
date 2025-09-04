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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MetricsFacade {

    private final ProductMetricsService productMetricsService;
    private final EventHandleLogService eventHandleService;

    @Transactional
    public void aggregate(MetricsCommand.Adjust command) {

        Optional<EventHandleLog> byEventId = eventHandleService.findByEventId(command.getEventId());
        if (byEventId.isPresent()) {
            log.info("이미 수행된 eventId: {}", command.getEventId());
            return;
        }

        LocalDate today = LocalDate.now();

        ProductMetrics productMetrics;
        Optional<ProductMetrics> metricsOptional = productMetricsService.findByProductIdAndMetricsDate(command.getProductId(),today);
        productMetrics = metricsOptional.orElseGet(() -> productMetricsService.save(command.getProductId()));
        switch (command.getEventType()){
            case LIKE_EVENT, UNLIKE_EVENT ->productMetrics.adjustLikeCount(command.getCount());
            case PRODUCT_SOLD_EVENT -> productMetrics.incrementSaleCount(command.getCount());
            case PRODUCT_VIEW_EVENT -> productMetrics.incrementViewCount(command.getCount());

        }
        eventHandleService.save(command.getEventId());
    }
}
