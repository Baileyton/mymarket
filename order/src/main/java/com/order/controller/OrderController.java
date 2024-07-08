package com.order.controller;

import com.order.dto.OrderRequestDto;
import com.order.dto.OrderResponseDto;
import com.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

//    @PostMapping
//    public ResponseEntity<?> order(@AuthenticationPrincipal UserDetailsImpl userDetails,
//                                   @Valid @RequestBody OrderRequestDto requestDto) {
//        Long userId = userDetails.getUser().getId(); // 인증된 사용자의 userId 가져오기
//        OrderResponseDto orderResponseDto = orderService.createOrder(userId, requestDto);
//        return ResponseEntity.ok(orderResponseDto);
//    }
//
//    @GetMapping
//    public ResponseEntity<?> getOrders(@AuthenticationPrincipal UserDetailsImpl userDetails){
//        Long userId = userDetails.getUser().getId();
//        List<OrderResponseDto> orderResponseDtos = orderService.getOrdersByUserId(userId);
//        return ResponseEntity.ok(orderResponseDtos);
//    }
//
//    @GetMapping("/{orderId}")
//    public ResponseEntity<?> getOrderById(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("orderId") Long id) {
//        Long userId = userDetails.getUser().getId();
//        OrderResponseDto orderResponseDto = orderService.getOrderById(userId, id);
//        return ResponseEntity.ok(orderResponseDto);
//    }
//
//    @DeleteMapping("/{orderId}")
//    public ResponseEntity<?> deleteOrderById(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("orderId") Long id) {
//        Long userId = userDetails.getUser().getId();
//        boolean deleted = orderService.deleteOrderById(userId, id);
//        if (!deleted) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
//        }
//        return ResponseEntity.ok().body("주문을 취소 했습니다.");
//    }
}