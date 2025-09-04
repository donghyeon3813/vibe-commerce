package com.loopers.domain.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventHandleLogService {
    private final EventHandleLogRepository eventHandleLogRepository;

    public Optional<EventHandleLog> findByEventId(String eventId) {
        return eventHandleLogRepository.findByEventId(eventId);
    }

    public void save(String eventId) {
        EventHandleLog eventHandleLog = EventHandleLog.of(eventId);
        eventHandleLogRepository.save(eventHandleLog);
    }
}
