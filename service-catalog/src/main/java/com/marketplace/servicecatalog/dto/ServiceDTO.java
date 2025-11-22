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

    // Alojamiento
    LocalDateTime startDate,
    LocalDateTime endDate,

    // Transporte
    String transportType,
    String routeOrigin,
    String routeDestination,

    // Coordenadas
    Double latitude,
    Double longitude,

    // Imágenes
    List<ServiceImageDTO> images,

    // 🔥 NUEVOS CAMPOS
    CountryInfoDTO country,
    MapsInfoDTO maps,
    WeatherReportDTO weather
) {}
