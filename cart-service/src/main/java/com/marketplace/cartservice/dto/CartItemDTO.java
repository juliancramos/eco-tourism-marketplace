package com.marketplace.cartservice.dto;

import java.time.LocalDateTime;

public record CartItemDTO (
    Long id,
    Long serviceId,
    Integer quantity,
    LocalDateTime dateAdded
){}
