package com.marketplace.cartservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.cartservice.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
