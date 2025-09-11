package com.loopers.domain.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class EventHandleLogService {
    private final EventHandleLogRepository eventHandleLogRepository;

    public Optional<EventHandleLog> findByEventIdAndConsumer(String eventId, String consumer) {
        return eventHandleLogRepository.findByEventIdAndConsumer(eventId, consumer);
    }

    public void save(String eventId, String topic) {
        EventHandleLog eventHandleLog = EventHandleLog.of(eventId, topic);
        eventHandleLogRepository.save(eventHandleLog);
    }

    public List<EventHandleLog> findByEventIdsAndConsumer(Set<String> eventIds, String consumer) {
        return eventHandleLogRepository.findByEventIdsAndConsumer(eventIds, consumer);
    }
}
