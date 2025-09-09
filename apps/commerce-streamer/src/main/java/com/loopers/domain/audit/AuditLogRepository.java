package com.loopers.domain.audit;

public interface AuditLogRepository {
    void save(AuditLog auditLog);
}
