package com.example.orderplatform.notification;

import com.example.orderplatform.events.DomainEvents.NotificationSentEvent;
import com.example.orderplatform.events.DomainEvents.ShipmentGeneratedEvent;
import com.example.orderplatform.events.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;
import java.util.UUID;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final EventPublisher eventPublisher;

    public NotificationService(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Async
    @TransactionalEventListener(fallbackExecution = true)
    public void sendShipmentNotification(ShipmentGeneratedEvent event) {
        log.info("Publishing SNS email/SMS notification orderId={} trackingNumber={}", event.orderId(), event.trackingNumber());
        eventPublisher.publish(new NotificationSentEvent(UUID.randomUUID(), event.orderId(), "EMAIL_SMS", Instant.now()));
    }
}
