package com.example.orderplatform.audit;

import com.example.orderplatform.events.DomainEvents.DomainEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class AuditService {
    private static final Logger log = LoggerFactory.getLogger(AuditService.class);
    private final AuditLogRepository auditLogs;
    private final ObjectMapper objectMapper;

    public AuditService(AuditLogRepository auditLogs, ObjectMapper objectMapper) {
        this.auditLogs = auditLogs;
        this.objectMapper = objectMapper;
    }

    @Async
    @TransactionalEventListener(fallbackExecution = true)
    public void save(DomainEvent event) throws JsonProcessingException {
        auditLogs.save(new AuditLogEntity(event.eventId(), event.orderId(), event.occurredAt(),
                event.eventType(), objectMapper.writeValueAsString(event)));
        log.info("Audit saved eventType={} eventId={}", event.eventType(), event.eventId());
    }
}
