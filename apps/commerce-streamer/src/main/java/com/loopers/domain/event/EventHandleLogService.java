package com.loopers.domain.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventHandleLogService {
    private final EventHandleLogRepository eventHandleLogRepository;

    public Optional<EventHandleLog> findByEventId(String eventId, String consumer) {
        return eventHandleLogRepository.findByEventIdAndConsumer(eventId, consumer);
    }

    public void save(String eventId, String topic) {
        EventHandleLog eventHandleLog = EventHandleLog.of(eventId, topic);
        eventHandleLogRepository.save(eventHandleLog);
    }
}
