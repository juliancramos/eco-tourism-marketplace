package com.marketplace.cartservice.service;

import com.marketplace.cartservice.dto.OrderDTO;

public interface OrderService {

    String updateOrderStatus(Long orderId, String status);
    OrderDTO getOrder(Long orderId);
    OrderDTO checkout(Long userId, String paymentMethod);

}
