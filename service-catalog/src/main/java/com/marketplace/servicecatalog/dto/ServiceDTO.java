package com.marketplace.servicecatalog.dto;

import java.time.Instant;
import java.util.List;

public record ServiceDTO(
    Long id,
    Long providerId,
    Long categoryId,
    String title,
    String description,
    Double price,
    String currency,
    boolean active,
    String countryCode,
    String cityCode,
    String address,
    Instant creationDate,
    List<ServiceImageDTO> images
) {}