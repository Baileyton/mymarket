package com.mymarket.dto;

import com.mymarket.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String name;
    private String category;
    private Integer price;
    private Integer quantity;
    private String description;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.quantity = product.getQuantity();
        this.description = product.getDescription();
    }
}
