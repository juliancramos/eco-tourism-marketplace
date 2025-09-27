package com.marketplace.cartservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.cartservice.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
