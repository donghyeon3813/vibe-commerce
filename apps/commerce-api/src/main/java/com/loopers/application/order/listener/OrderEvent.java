package com.loopers.application.order.listener;

import lombok.Getter;
import lombok.ToString;

public class OrderEvent {
    @Getter
    @ToString
    public static class StatusUpdateEvent {
        private Long id;
        private String status;
        private StatusUpdateEvent(Long id, String status) {
            this.id = id;
            this.status = status;
        }
        public static StatusUpdateEvent create(Long id, String status) {
            return new StatusUpdateEvent(id, status);
        }
    }
}
