package com.marketplace.cartservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.cartservice.model.Order;

public interface  OrderRepository extends JpaRepository<Order, Long> {

}
