package com.loopers.domain.cache;

import com.loopers.application.domain.CacheService;
import com.loopers.domain.event.EventHandleLog;
import com.loopers.domain.event.EventHandleLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CacheFacade {

    private final EventHandleLogService eventHandleService;
    private final CacheService cacheService;

    public void evict(CacheCommand.EvictCache command) {
        Optional<EventHandleLog> byEventId = eventHandleService.findByEventIdAndConsumer(command.getEventId(), command.getConsumer());
        if (byEventId.isPresent()) {
            log.info("이미 수행된 eventId: {}", command.getEventId());
            return;
        }
        cacheService.EvictProductCache(command);
        eventHandleService.save(command.getEventId(), command.getConsumer());
    }
}
