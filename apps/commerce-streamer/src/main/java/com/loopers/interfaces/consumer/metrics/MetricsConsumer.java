package com.loopers.interfaces.consumer.metrics;

import com.loopers.application.metrics.MetricsCommand;
import com.loopers.application.metrics.MetricsFacade;
import com.loopers.confg.kafka.KafkaConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class MetricsConsumer {
    private final MetricsFacade metricsFacade;

    @KafkaListener(
            topics = "catalog-events",
            groupId = "metrics-group",
            concurrency = "3",
            containerFactory = KafkaConfig.BATCH_LISTENER)
    public void consume(List<MetricsEvent> messages, Acknowledgment acknowledgment) {
        log.info("Consumed message: {}", messages.size());

        metricsFacade.aggregate(
                messages.stream()
                        .map(event -> MetricsCommand.Adjust.of(
                                event.getEventId(),
                                event.getProductId(),
                                MetricsCommand.Adjust.EventType.getEventType(event.getType().name()),
                                event.getCount(),
                                "MetricsConsumer-Batch"
                        ))
                        .toList()
        );
        acknowledgment.acknowledge();
    }
}
