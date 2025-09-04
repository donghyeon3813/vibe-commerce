package com.loopers.infrastructure.audit;

import com.loopers.domain.audit.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogJpaRepostiroy extends JpaRepository<AuditLog, Long> {
}
