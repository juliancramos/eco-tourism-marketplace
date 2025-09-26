package com.marketplace.cartservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.cartservice.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(Long userId);
}
