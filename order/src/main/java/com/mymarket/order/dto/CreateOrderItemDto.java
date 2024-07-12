package com.mymarket.order.dto;

import com.mymarket.domain.item.Item;
import com.mymarket.domain.OrderItem;
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
