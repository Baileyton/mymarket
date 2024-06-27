package com.mymarket.service;


import com.mymarket.dto.OrderRequestDto;
import com.mymarket.entity.Order;
import com.mymarket.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
                .status("상품주문")
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .modifiedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        return orderRepository.save(order);
    }

    @Scheduled(cron = "0 0 1 * * ?") // 매일 새벽 1시에 실행
    public void updateOrderStatuses() {
        LocalDateTime now = LocalDateTime.now();
        List<Order> orders = orderRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Order order : orders) {
            updateOrderStatus(order, now, formatter);
        }
    }

    public Order getOrderStatus(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        updateOrderStatus(order, now, formatter);
        return order;
    }

    private void updateOrderStatus(Order order, LocalDateTime now, DateTimeFormatter formatter) {
        LocalDateTime createdAt = LocalDateTime.parse(order.getCreatedAt(), formatter);
        long daysBetween = ChronoUnit.DAYS.between(createdAt, now);
        if (daysBetween == 1 && order.getStatus().equals("상품주문")) {
            order.updateStatus("배송중");
            orderRepository.save(order);
        } else if (daysBetween == 2 && order.getStatus().equals("배송중")) {
            order.updateStatus("배송완료");
            orderRepository.save(order);
        }
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
