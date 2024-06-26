package com.mymarket.repository;

import com.mymarket.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserIdAndProductId(Long userId, Long productId);

    List<Cart> findByUserId(long userId);

    Optional<Cart> findByIdAndUserId(Long id, Long userId);
}
