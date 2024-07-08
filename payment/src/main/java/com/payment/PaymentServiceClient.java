package com.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "payment-service") // 호출할 대상 서비스 이름
public interface PaymentServiceClient {

    @PostMapping("/payments/process") // 결제 서비스의 API 엔드포인트
    boolean processPayment(@RequestParam("orderId") Long orderId,
                           @RequestParam("amount") BigDecimal amount);
}