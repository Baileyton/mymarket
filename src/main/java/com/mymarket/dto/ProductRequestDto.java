package com.mymarket.dto;

import lombok.Getter;

@Getter
public class ProductRequestDto {
    private String name;
    private String category;
    private String description;
    private Integer price;
    private Integer quantity;
}
