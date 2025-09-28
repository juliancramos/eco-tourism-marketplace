package com.marketplace.Payment_Service.dto.OrderDTOs;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO(Long orderId,
    Long id,
    Long cartId,
    Long userId,
    Double total,
    String status,
    LocalDateTime orderDate,
    List<OrderItemDTO> items
) {
}
