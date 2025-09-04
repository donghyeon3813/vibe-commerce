package com.loopers.interfaces.consumer.metrics;

import com.loopers.application.metrics.MetricsCommand;
import com.loopers.application.metrics.MetricsFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class MetricsConsumer {
    private final MetricsFacade metricsFacade;

    @KafkaListener(topics = "catalog-events", groupId = "metrics-group")
    public void consume(MetricsEvent message) {
        log.info("Consumed message: {}", message);

        metricsFacade.aggregate(MetricsCommand.Adjust.of(
                message.getEventId(),
                message.getProductId(),
                MetricsCommand.Adjust.EventType.getEventType(message.getType().name()),
                message.getCount()));
    }
}
