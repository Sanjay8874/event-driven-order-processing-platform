package com.example.orderplatform;

import com.example.orderplatform.events.EventPublisher;
import com.example.orderplatform.order.OrderDtos.CreateOrderRequest;
import com.example.orderplatform.order.OrderDtos.OrderItemRequest;
import com.example.orderplatform.order.OrderRepository;
import com.example.orderplatform.order.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    OrderRepository orders;
    @Mock
    EventPublisher eventPublisher;

    @Test
    void createsOrderAndPublishesEvent() {
        var service = new OrderService(orders, eventPublisher);
        when(orders.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.create(new CreateOrderRequest(UUID.randomUUID(),
                List.of(new OrderItemRequest(UUID.randomUUID(), 2, new BigDecimal("19.50")))));

        assertThat(response.totalAmount()).isEqualByComparingTo("39.00");
        verify(eventPublisher).publish(any());
    }

    @Test
    void getsExistingOrder() {
        var service = new OrderService(orders, eventPublisher);
        var saved = new com.example.orderplatform.order.OrderEntity(UUID.randomUUID());
        saved.addItem(UUID.randomUUID(), 1, new BigDecimal("10.00"));
        when(orders.findById(saved.getId())).thenReturn(Optional.of(saved));

        assertThat(service.get(saved.getId()).items()).hasSize(1);
    }
}
