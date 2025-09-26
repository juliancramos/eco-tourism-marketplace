package com.marketplace.cartservice.dto;

public record AddCartItemDTO (
    Long serviceId, 
    Integer quantity
    ) {}
