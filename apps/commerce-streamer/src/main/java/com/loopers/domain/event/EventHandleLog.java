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

    public static EventHandleLog of(String eventId) {
        return new EventHandleLog(eventId);
    }

    private EventHandleLog(String eventId) {
        this.eventId = eventId;
    }
}
