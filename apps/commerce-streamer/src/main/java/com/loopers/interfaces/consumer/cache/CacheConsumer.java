package com.loopers.interfaces.consumer.cache;

import com.loopers.application.cache.CacheCommand;
import com.loopers.application.cache.CacheFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CacheConsumer {
    private final CacheFacade cacheFacade;

    @KafkaListener(topics = "cache-evicts-events", groupId = "cache-group", concurrency = "3")
    public void consume(CacheEvent message) {
        log.info("Consumed message: {}", message);
        CacheCommand.EvictCache evictCache = CacheCommand.EvictCache
                .create(message.getEventId(), message.getProductId(), "CacheConsumer");
        cacheFacade.evict(evictCache);
    }
}
