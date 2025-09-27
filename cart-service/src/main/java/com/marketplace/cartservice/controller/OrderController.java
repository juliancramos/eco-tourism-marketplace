package com.marketplace.cartservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.cartservice.dto.OrderDTO;
import com.marketplace.cartservice.service.MQCheckoutService;
import com.marketplace.cartservice.service.OrderService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Autowired
    private final MQCheckoutService mqCheckoutService;

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<OrderDTO> checkout(@PathVariable Long userId) {
        OrderDTO dto = this.orderService.checkout(userId);
        this.mqCheckoutService.sendMessage(dto);
        return ResponseEntity.status(201).body(dto);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long orderId) {
        OrderDTO dto = this.orderService.getOrder(orderId);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long orderId,
            @RequestBody String req) {
        String updated = this.orderService.updateOrderStatus(orderId, req);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable Long userId) {
        List<OrderDTO> list = ((com.marketplace.cartservice.service.impl.OrderServiceImpl) orderService)
                .findOrdersByUser(userId);
        return ResponseEntity.ok(list);
    }

}
