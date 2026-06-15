package com.example.orderplatform.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class DomainEvents {
    private DomainEvents() {
    }

    public interface DomainEvent {
        UUID eventId();
        UUID orderId();
        String eventType();
        Instant occurredAt();
    }

    public record OrderLine(UUID inventoryId, int quantity, BigDecimal unitPrice) {
    }

    public record OrderCreatedEvent(UUID eventId, UUID orderId, List<OrderLine> lines, BigDecimal totalAmount,
                                    Instant occurredAt) implements DomainEvent {
        public String eventType() { return "ORDER_CREATED"; }
    }

    public record InventoryReservedEvent(UUID eventId, UUID orderId, BigDecimal totalAmount,
                                         Instant occurredAt) implements DomainEvent {
        public String eventType() { return "INVENTORY_RESERVED"; }
    }

    public record PaymentProcessedEvent(UUID eventId, UUID orderId, String paymentReference,
                                        Instant occurredAt) implements DomainEvent {
        public String eventType() { return "PAYMENT_PROCESSED"; }
    }

    public record ShipmentGeneratedEvent(UUID eventId, UUID orderId, String trackingNumber,
                                         Instant occurredAt) implements DomainEvent {
        public String eventType() { return "SHIPMENT_GENERATED"; }
    }

    public record NotificationSentEvent(UUID eventId, UUID orderId, String channel,
                                        Instant occurredAt) implements DomainEvent {
        public String eventType() { return "NOTIFICATION_SENT"; }
    }
}
