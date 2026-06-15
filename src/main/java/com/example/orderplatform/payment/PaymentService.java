package com.example.orderplatform.payment;

import com.example.orderplatform.events.DomainEvents.InventoryReservedEvent;
import com.example.orderplatform.events.DomainEvents.PaymentProcessedEvent;
import com.example.orderplatform.events.EventPublisher;
import com.example.orderplatform.order.OrderService;
import com.example.orderplatform.order.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;
import java.util.UUID;

@Service
public class PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository payments;
    private final OrderService orderService;
    private final EventPublisher eventPublisher;

    public PaymentService(PaymentRepository payments, OrderService orderService, EventPublisher eventPublisher) {
        this.payments = payments;
        this.orderService = orderService;
        this.eventPublisher = eventPublisher;
    }

    @Async
    @TransactionalEventListener(fallbackExecution = true)
    @Retryable(retryFor = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay = 500, multiplier = 2))
    @Transactional
    public void processPayment(InventoryReservedEvent event) {
        log.info("Processing mock payment orderId={} amount={}", event.orderId(), event.totalAmount());
        PaymentEntity payment = payments.findByOrderId(event.orderId())
                .orElseGet(() -> payments.save(new PaymentEntity(event.orderId(), event.totalAmount())));
        payment.markPaid("MOCK-" + UUID.randomUUID());
        orderService.mark(event.orderId(), OrderStatus.PAID);
        eventPublisher.publish(new PaymentProcessedEvent(UUID.randomUUID(), event.orderId(), payment.getPaymentReference(), Instant.now()));
    }

    @Recover
    public void sendToDeadLetter(RuntimeException ex, InventoryReservedEvent event) {
        log.error("Payment failed after retries; routing to DLQ orderId={}", event.orderId(), ex);
        orderService.mark(event.orderId(), OrderStatus.FAILED);
    }
}
