package com.loopers.domain.event;

import java.util.Optional;

public interface EventHandleLogRepository {
    Optional<EventHandleLog> findByEventIdAndConsumer(String eventId, String topic);

    void save(EventHandleLog eventHandleLog);


}
