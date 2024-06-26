package com.mymarket.service;


import com.mymarket.dto.OrderRequestDto;
import com.mymarket.entity.Order;
import com.mymarket.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order order(Long userId, OrderRequestDto requestDto) {
        // 주문 생성
        Order order = Order.builder()
                .userId(userId)
                .status("Unpaid")
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .modifiedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        return orderRepository.save(order);
    }

    public List<Order> getOrderListByUserId(long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order getOrderById(Long userId, Long orderId) {
        Optional<Order> orderOptional = orderRepository.findByIdAndUserId(orderId, userId);
        return orderOptional.orElse(null);
    }

    public boolean deleteOrderById(Long userId, Long orderId) {
        Optional<Order> orderOptional = orderRepository.findByIdAndUserId(orderId, userId);
        if (orderOptional.isPresent()) {
            orderRepository.delete(orderOptional.get());
            return true;
        }
        return false;
    }
}
