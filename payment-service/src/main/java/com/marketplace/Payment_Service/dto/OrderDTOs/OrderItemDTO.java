package com.marketplace.Payment_Service.dto.OrderDTOs;

public record OrderItemDTO(
    Long id,
    Long serviceId,
    Integer quantity,
    Double price
) {
    
}
