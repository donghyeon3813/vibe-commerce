package com.loopers.domain.event;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventHandleLogRepository {
    Optional<EventHandleLog> findByEventIdAndConsumer(String eventId, String topic);

    void save(EventHandleLog eventHandleLog);


    List<EventHandleLog> findByEventIdsAndConsumer(Set<String> eventIds, String consumer);
}
