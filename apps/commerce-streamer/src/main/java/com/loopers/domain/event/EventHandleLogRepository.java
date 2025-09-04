package com.loopers.domain.event;

import java.util.Optional;

public interface EventHandleLogRepository {
    Optional<EventHandleLog> findByEventId(String eventId);

    void save(EventHandleLog eventHandleLog);
}
