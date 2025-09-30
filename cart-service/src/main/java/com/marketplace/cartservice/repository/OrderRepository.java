package com.marketplace.cartservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.cartservice.model.Order;

public interface  OrderRepository extends JpaRepository<Order, Long> {
    Order findByCartId(Long cartId);
    List<Order> findByUserId(Long userId);
    
}
