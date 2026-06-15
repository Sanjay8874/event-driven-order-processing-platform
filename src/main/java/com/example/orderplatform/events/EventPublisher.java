package com.example.orderplatform.events;

import com.example.orderplatform.events.DomainEvents.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {
    private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);
    private final ApplicationEventPublisher publisher;

    public EventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(DomainEvent event) {
        log.info("Publishing domain event type={} orderId={} eventId={}", event.eventType(), event.orderId(), event.eventId());
        publisher.publishEvent(event);
    }
}
