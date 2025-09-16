package com.loopers.interfaces.consumer.cache;

import com.loopers.domain.cache.CacheCommand;
import com.loopers.domain.cache.CacheFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CacheConsumer {
    private final CacheFacade cacheFacade;

    @KafkaListener(topics = "cache-evicts-events", groupId = "cache-group", concurrency = "3")
    public void consume(CacheEvent message, Acknowledgment ack) {
        log.info("Consumed message: {}", message);
        CacheCommand.EvictCache evictCache = CacheCommand.EvictCache
                .create(message.getEventId(), message.getProductId(), "CacheConsumer");
        cacheFacade.evict(evictCache);
        ack.acknowledge();
    }
}
