package com.loopers.domain.audit;

import com.loopers.application.audit.AuditCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditService {
    private final AuditLogRepository auditLogRepository;
    public void save(AuditCommand.SaveAudit saveAudit) {
        AuditLog auditLog = AuditLog.create(saveAudit.getTopic(),
                saveAudit.getPartition(),
                saveAudit.getOffset(),
                saveAudit.getKey(),
                saveAudit.getPayload(),
                saveAudit.getTimestamp());
        auditLogRepository.save(auditLog);

    }
}
