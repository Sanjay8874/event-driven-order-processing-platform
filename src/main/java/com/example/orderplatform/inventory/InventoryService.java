package com.example.orderplatform.inventory;

import com.example.orderplatform.common.Exceptions.ResourceNotFoundException;
import com.example.orderplatform.events.DomainEvents.InventoryReservedEvent;
import com.example.orderplatform.events.DomainEvents.OrderCreatedEvent;
import com.example.orderplatform.events.EventPublisher;
import com.example.orderplatform.order.OrderService;
import com.example.orderplatform.order.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;
import java.util.UUID;

@Service
public class InventoryService {
    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);
    private final InventoryRepository inventory;
    private final OrderService orderService;
    private final EventPublisher eventPublisher;

    public InventoryService(InventoryRepository inventory, OrderService orderService, EventPublisher eventPublisher) {
        this.inventory = inventory;
        this.orderService = orderService;
        this.eventPublisher = eventPublisher;
    }

    @Async
    @TransactionalEventListener(fallbackExecution = true)
    @Transactional
    public void reserveForOrder(OrderCreatedEvent event) {
        log.info("Reserving inventory orderId={}", event.orderId());
        event.lines().forEach(line -> item(line.inventoryId()).reserve(line.quantity()));
        orderService.mark(event.orderId(), OrderStatus.PAYMENT_PENDING);
        eventPublisher.publish(new InventoryReservedEvent(UUID.randomUUID(), event.orderId(), event.totalAmount(), Instant.now()));
    }

    @Transactional
    public InventoryItem updateStock(UUID id, int quantity) {
        InventoryItem item = item(id);
        item.updateStock(quantity);
        return item;
    }

    private InventoryItem item(UUID id) {
        return inventory.findById(id).orElseThrow(() -> new ResourceNotFoundException("Inventory item not found: " + id));
    }
}
