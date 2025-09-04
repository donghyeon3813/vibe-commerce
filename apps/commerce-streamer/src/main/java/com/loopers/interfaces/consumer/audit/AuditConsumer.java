package com.loopers.interfaces.consumer.audit;

import com.loopers.application.audit.AuditCommand;
import com.loopers.application.audit.AuditFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditConsumer {
    private final AuditFacade auditFacade;

    @KafkaListener(topics = "catalog-events", groupId = "audit-group")
    public void consume(ConsumerRecord<Object, Object> messages) {
        log.info("message" + messages.toString());

        AuditCommand.SaveAudit saveAudit = AuditCommand.SaveAudit.of(
                messages.topic(),
                messages.partition(),
                messages.offset(),
                messages.key().toString(),
                messages.value().toString(),
                messages.timestamp());
        auditFacade.save(saveAudit);

    }
}
