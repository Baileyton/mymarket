package com.mymarket.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderProductDto {
    @NotNull
    private Long productId;
    @NotNull
    private Integer quantity;
}
