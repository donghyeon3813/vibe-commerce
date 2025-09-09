package com.loopers.infrastructure.event;

import com.loopers.domain.event.EventHandleLog;
import com.loopers.domain.event.EventHandleLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class EventHandleLogRepositoryImpl implements EventHandleLogRepository {
    private final EventHandleLogJpaRepository repository;
    @Override
    public Optional<EventHandleLog> findByEventIdAndConsumer(String eventId, String consumer) {
        return repository.findByEventIdAndConsumer(eventId, consumer);
    }

    @Override
    public void save(EventHandleLog eventHandleLog) {
        repository.save(eventHandleLog);
    }

    @Override
    public List<EventHandleLog> findByEventIdsAndConsumer(Set<String> eventIds, String consumer) {
        return repository.findByEventIdInAndConsumer(eventIds, consumer);
    }
}
