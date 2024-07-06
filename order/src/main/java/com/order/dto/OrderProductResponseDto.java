package com.order.dto;

import lombok.Getter;

@Getter
public class OrderProductResponseDto {
    private Long productId;
    private Integer quantity;

    public OrderProductResponseDto(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}