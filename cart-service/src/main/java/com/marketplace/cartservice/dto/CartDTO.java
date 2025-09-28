package com.marketplace.cartservice.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CartDTO(
        Long id,
        Long userId,
        LocalDateTime creationDate,
        List<CartItemDTO> items) {}
