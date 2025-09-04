package com.loopers.application.audit;

import com.loopers.domain.audit.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditFacade {
    private final AuditService auditService;

    public void save(AuditCommand.SaveAudit saveAudit) {
        log.info("Saving audit {}", saveAudit);
        auditService.save(saveAudit);

    }
}
