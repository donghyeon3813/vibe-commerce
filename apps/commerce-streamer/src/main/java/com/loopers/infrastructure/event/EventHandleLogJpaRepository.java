package com.loopers.infrastructure.event;

import com.loopers.domain.event.EventHandleLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventHandleLogJpaRepository extends JpaRepository<EventHandleLog, Long> {
    Optional<EventHandleLog> findByEventIdAndTopic(String eventId, String topic);
}
