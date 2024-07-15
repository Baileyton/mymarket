package com.order.dto;

import com.order.entity.Item;
import com.order.entity.OrderItem;
import lombok.Getter;

@Getter
public class CreateOrderItemDto {
    private Long itemId;
    private int count;

    public OrderItem toEntity(Item item) {
        return OrderItem.builder()
                .item(item)
                .orderPrice(item.getPrice() * count)
                .count(count)
                .build();
    }
}
