package com.mymarket.contoller;

import com.mymarket.dto.OrderRequestDto;
import com.mymarket.entity.Cart;
import com.mymarket.entity.Order;
import com.mymarket.security.UserDetailsImpl;
import com.mymarket.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<?> order(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @Valid @RequestBody OrderRequestDto requestDto) {
        Long userId = userDetails.getUser().getId(); // 인증된 사용자의 userId 가져오기
        Order order = orderService.order(userId, requestDto);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Long userId = userDetails.getUser().getId();
        List<Order> orderList = orderService.getOrderListByUserId(userId);
        return ResponseEntity.ok(orderList);
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<?> getOrderById(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        Long userId = userDetails.getUser().getId();
        Order order = orderService.getOrderById(userId, id);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<?> deleteOrderById(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        Long userId = userDetails.getUser().getId();
        boolean deleted = orderService.deleteOrderById(userId, id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }
        return ResponseEntity.ok().body("주문을 취소 했습니다.");
    }
}
