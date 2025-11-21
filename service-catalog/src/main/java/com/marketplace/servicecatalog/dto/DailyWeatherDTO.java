package com.marketplace.servicecatalog.dto;

import java.time.LocalDateTime;

public record DailyWeatherDTO(
    LocalDateTime date,
    Double temperature,
    String condition
) {}