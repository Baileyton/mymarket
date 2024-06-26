package com.mymarket.repository;

import com.mymarket.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(long userId);
    Optional<Order> findByIdAndUserId(Long id, Long userId);
}
