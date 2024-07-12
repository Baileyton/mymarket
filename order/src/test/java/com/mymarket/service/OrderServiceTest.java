package com.mymarket.service;

import com.mymarket.domain.Order;
import com.mymarket.domain.OrderStatus;
import com.mymarket.domain.item.Item;
import com.mymarket.domain.item.ReservedItem;
import com.mymarket.order.repository.OrderRepository;
import com.mymarket.order.service.OrderService;
import com.mymarket.user.domain.User;
import com.mymarket.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문 처리")
    void processOrderTest() {
        User user = new User();
        Item item = new ReservedItem();
        Order order = new Order();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderStatus status = orderService.processOrder(1L, 1L);
        assertNotNull(status);
        verify(orderRepository).save(any(Order.class));
    }
}
