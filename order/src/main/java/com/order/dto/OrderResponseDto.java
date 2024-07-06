package com.order.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private String status;
    private String createdAt;
    private String modifiedAt;
    private List<OrderProductResponseDto> orderProducts;

    public OrderResponseDto(Long id, Long userId, String status, String createdAt, String modifiedAt, List<OrderProductResponseDto> orderProducts) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.orderProducts = orderProducts;
    }
}
