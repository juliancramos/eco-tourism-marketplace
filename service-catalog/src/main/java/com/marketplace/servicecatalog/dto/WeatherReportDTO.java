package com.marketplace.servicecatalog.dto;

import java.util.List;

public record WeatherReportDTO(
    String summary,
    Double minTemp,
    Double maxTemp,
    List<DailyWeatherDTO> forecast
) {}