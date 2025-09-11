package com.loopers.infrastructure.event;

import com.loopers.domain.event.EventHandleLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventHandleLogJpaRepository extends JpaRepository<EventHandleLog, Long> {

    List<EventHandleLog> findByEventIdInAndConsumer(Set<String> eventIds, String consumer);

    Optional<EventHandleLog> findByEventIdAndConsumer(String eventId, String consumer);
}
