package com.loopers.infrastructure.audit;

import com.loopers.domain.audit.AuditLog;
import com.loopers.domain.audit.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditLogRepositoryImpl implements AuditLogRepository {

    private final AuditLogJpaRepostiroy auditLogJpaRepostiroy;

    public void save(AuditLog auditLog) {
        auditLogJpaRepostiroy.save(auditLog);
    }
}
