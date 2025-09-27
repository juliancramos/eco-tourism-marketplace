package com.marketplace.cartservice.dto;

public record OrderItemDTO (
    Long id,
    Long serviceId, 
    Integer quantity,
    Double price
    ) {}
