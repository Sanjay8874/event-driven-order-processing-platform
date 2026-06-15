package com.example.orderplatform.audit;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
public class AuditLogEntity {
    @Id
    private UUID eventId;
    @Column(nullable = false)
    private UUID orderId;
    @Column(nullable = false)
    private Instant timestamp;
    @Column(nullable = false)
    private String eventType;
    @Column(nullable = false, columnDefinition = "jsonb")
    private String payload;

    protected AuditLogEntity() {
    }

    public AuditLogEntity(UUID eventId, UUID orderId, Instant timestamp, String eventType, String payload) {
        this.eventId = eventId;
        this.orderId = orderId;
        this.timestamp = timestamp;
        this.eventType = eventType;
        this.payload = payload;
    }
}
