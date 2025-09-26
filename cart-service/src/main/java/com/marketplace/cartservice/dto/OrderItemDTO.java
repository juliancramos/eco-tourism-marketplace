package com.marketplace.cartservice.dto;

public record OrderItemDTO (
    Long serviceId, 
    Integer quantity,
    Double price
    ) {}
