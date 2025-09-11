package com.loopers.domain.event;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "event_handle_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventHandleLog extends BaseEntity {
    private String eventId;
    private String consumer;

    public static EventHandleLog of(String eventId, String consumer) {
        return new EventHandleLog(eventId, consumer);
    }

    private EventHandleLog(String eventId, String consumer) {
        this.eventId = eventId;
        this.consumer = consumer;
    }
}
