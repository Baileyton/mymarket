package com.mymarket.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CartQuantityDto {
    @NotNull
    private Integer quantity;
}
