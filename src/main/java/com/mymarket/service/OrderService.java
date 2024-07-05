package com.mymarket.service;


import com.mymarket.dto.OrderProductResponseDto;
import com.mymarket.dto.OrderRequestDto;
import com.mymarket.dto.OrderResponseDto;
import com.mymarket.entity.Order;
import com.mymarket.entity.OrderProduct;
import com.mymarket.repository.OrderRepository;
import com.mymarket.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderResponseDto createOrder(Long userId, OrderRequestDto requestDto) {
        // 주문 생성
        Order order = Order.builder()
                .userId(userId)
                .status("상품주문")
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .modifiedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        List<OrderProduct> orderProducts = requestDto.getProductInfo().stream()
                .map(productDto -> OrderProduct.builder()
                        .order(order)
                        .productId(productDto.getProductId())
                        .price(productRepository.findById(productDto.getProductId()).orElseThrow(() ->
                                new IllegalArgumentException("Product not found")).getPrice())
                        .quantity(productDto.getQuantity())
                        .build())
                .collect(Collectors.toList());

        order.setOrderProducts(orderProducts);
        orderRepository.save(order);

        List<OrderProductResponseDto> orderProductResponseDtos = orderProducts.stream()
                .map(op -> new OrderProductResponseDto(op.getProductId(), op.getQuantity()))
                .toList();

        return new OrderResponseDto(order.getId(), order.getUserId(), order.getStatus(), order.getCreatedAt(), order.getModifiedAt(), orderProductResponseDtos);
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(order -> {
            List<OrderProductResponseDto> orderProductResponseDtos = order.getOrderProducts().stream()
                    .map(op -> new OrderProductResponseDto(op.getProductId(), op.getQuantity()))
                    .collect(Collectors.toList());
            return new OrderResponseDto(order.getId(), order.getUserId(), order.getStatus(), order.getCreatedAt(), order.getModifiedAt(), orderProductResponseDtos);
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        List<OrderProductResponseDto> orderProductResponseDtos = order.getOrderProducts().stream()
                .map(op -> new OrderProductResponseDto(op.getProductId(), op.getQuantity()))
                .collect(Collectors.toList());

        return new OrderResponseDto(order.getId(), order.getUserId(), order.getStatus(), order.getCreatedAt(), order.getModifiedAt(), orderProductResponseDtos);
    }

    @Transactional
    public boolean deleteOrderById(Long userId, Long orderId) {
        Optional<Order> orderOptional = orderRepository.findByIdAndUserId(orderId, userId);
        if (orderOptional.isPresent()) {
            orderRepository.delete(orderOptional.get());
            return true;
        }
        return false;
    }
}
