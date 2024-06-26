package com.mymarket.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequestDto {
    @NotNull
    private List<OrderProductDto> productInfo;
}
