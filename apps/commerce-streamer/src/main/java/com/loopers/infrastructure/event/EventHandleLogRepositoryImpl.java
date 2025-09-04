package com.loopers.infrastructure.event;

import com.loopers.domain.event.EventHandleLog;
import com.loopers.domain.event.EventHandleLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventHandleLogRepositoryImpl implements EventHandleLogRepository {
    private final EventHandleLogJpaRepository repository;
    @Override
    public Optional<EventHandleLog> findByEventId(String eventId) {
        return repository.findByEventId(eventId);
    }

    @Override
    public void save(EventHandleLog eventHandleLog) {
        repository.save(eventHandleLog);
    }
}
