package com.loopers.application.audit;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;


public class AuditCommand {
    @ToString
    @Getter
    public static class SaveAudit{
        private String topic;
        private int partition;
        private long offset;
        private String key;
        private String payload;
        private ZonedDateTime timestamp;

        public SaveAudit(String topic, int partition, long offset, String key, String payload, ZonedDateTime timestamp) {
            this.topic = topic;
            this.partition = partition;
            this.offset = offset;
            this.key = key;
            this.payload = payload;
            this.timestamp = timestamp;
        }

        public static SaveAudit of(String topic, int partition, long offset,
                                   String key, String payload, long timestamp) {
            return new SaveAudit(
                    topic,
                    partition,
                    offset,
                    key,
                    payload,
                    Instant.ofEpochMilli(timestamp).atZone(ZoneId.of("Asia/Seoul"))
            );
        }
    }
}
