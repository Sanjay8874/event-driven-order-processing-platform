package com.example.orderplatform.shipment;

import com.example.orderplatform.events.DomainEvents.PaymentProcessedEvent;
import com.example.orderplatform.events.DomainEvents.ShipmentGeneratedEvent;
import com.example.orderplatform.events.EventPublisher;
import com.example.orderplatform.order.OrderService;
import com.example.orderplatform.order.OrderStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;
import java.util.UUID;

@Service
public class ShipmentService {
    private final ShipmentRepository shipments;
    private final OrderService orderService;
    private final EventPublisher eventPublisher;

    public ShipmentService(ShipmentRepository shipments, OrderService orderService, EventPublisher eventPublisher) {
        this.shipments = shipments;
        this.orderService = orderService;
        this.eventPublisher = eventPublisher;
    }

    @Async
    @TransactionalEventListener(fallbackExecution = true)
    @Transactional
    public void createShipment(PaymentProcessedEvent event) {
        ShipmentEntity shipment = shipments.save(new ShipmentEntity(event.orderId(), "TRK-" + UUID.randomUUID()));
        orderService.mark(event.orderId(), OrderStatus.SHIPPED);
        eventPublisher.publish(new ShipmentGeneratedEvent(UUID.randomUUID(), event.orderId(), shipment.getTrackingNumber(), Instant.now()));
    }
}
