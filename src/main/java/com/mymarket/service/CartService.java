package com.mymarket.service;


import com.mymarket.dto.CartRequestDto;
import com.mymarket.entity.Cart;
import com.mymarket.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public Cart addToCart(Long userId, CartRequestDto requestDto) {
        Optional<Cart> existingCart = cartRepository.findByUserIdAndProductId(userId, requestDto.getProductId());
        if (existingCart.isPresent()) {
            throw new IllegalArgumentException("이미 장바구니에 등록된 상품입니다.");
        }

        Cart cart = Cart.builder()
                .userId(userId)
                .productId(requestDto.getProductId())
                .quantity(requestDto.getQuantity())
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        return cartRepository.save(cart);
    }

    public List<Cart> getCartListByUserId(long userId) {
        return cartRepository.findByUserId(userId);
    }
}
