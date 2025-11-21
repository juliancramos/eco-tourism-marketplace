package com.marketplace.servicecatalog.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ServiceDTO(
    Long id,
    Long providerId,
    Long categoryId,
    String title,
    String description,
    Double price,
    String currency,
    Boolean active,
    String countryCode,
    String cityCode,
    String address,
    LocalDateTime creationDate,

    // NUEVO: alojamiento
    LocalDateTime startDate,
    LocalDateTime endDate,

    // NUEVO: transporte
    String transportType,
    String routeOrigin,
    String routeDestination,

    // NUEVO: coordenadas
    Double latitude,
    Double longitude,

    List<ServiceImageDTO> images
) {}
