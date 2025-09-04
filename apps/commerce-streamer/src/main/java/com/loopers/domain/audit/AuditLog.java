package com.loopers.domain.audit;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "audit_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuditLog extends BaseEntity {
    private String topic;
    @Column(name = "kafka_partition")
    private int partition;
    @Column(name = "kafka_offset")
    private long offset;
    @Column(name = "kafka_key")
    private String key;
    private String payload;
    @Column(name = "received_at")
    private ZonedDateTime receivedAt;

    public static AuditLog create(String topic, int partition, long offset, String key, String payload, ZonedDateTime receivedAt) {
        return new AuditLog(topic, partition, offset, key, payload, receivedAt);
    }

    public AuditLog(String topic, int partition, long offset, String key, String payload, ZonedDateTime receivedAt) {
        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
        this.key = key;
        this.payload = payload;
        this.receivedAt = receivedAt;
    }
}
