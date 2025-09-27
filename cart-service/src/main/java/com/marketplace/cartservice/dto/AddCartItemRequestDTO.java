package com.marketplace.cartservice.dto;

public record AddCartItemRequestDTO (
    Long serviceId, 
    Integer quantity
    ) {}
