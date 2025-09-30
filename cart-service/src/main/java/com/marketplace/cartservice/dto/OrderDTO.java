package com.marketplace.cartservice.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO (
    Long id,
    Long cartId,
    Long userId,
    Double total,
    String status,
    String paymentMethod,
    LocalDateTime orderDate,
    List<OrderItemDTO> items
){}

