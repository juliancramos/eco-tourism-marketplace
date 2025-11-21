package com.marketplace.servicecatalog.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.marketplace.servicecatalog.dto.DailyWeatherDTO;
import com.marketplace.servicecatalog.dto.WeatherReportDTO;

@Service
public class WeatherApiClient {

    @Value("${weather.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.create();

    public WeatherReportDTO getWeather(Double lat, Double lon) {

        if (lat == null || lon == null) return null;

        String url = "https://api.openweathermap.org/data/2.5/forecast";

        Map response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(url)
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null) return null;

        List list = (List) response.get("list");
        if (list == null) return null;

        // Convert to daily forecast
        List<DailyWeatherDTO> daily = (List<DailyWeatherDTO>) list.stream()
                .map(item -> {
                    Map entry = (Map) item;

                    Long ts = ((Number) entry.get("dt")).longValue();
                    LocalDateTime date = LocalDateTime.ofInstant(
                            Instant.ofEpochSecond(ts),
                            ZoneId.systemDefault()
                    );

                    Map main = (Map) entry.get("main");
                    Double temp = ((Number) main.get("temp")).doubleValue();

                    List weatherList = (List) entry.get("weather");
                    Map weather = (Map) weatherList.get(0);
                    String condition = weather.get("description").toString();

                    return new DailyWeatherDTO(date, temp, condition);
                })
                .collect(Collectors.toList());

        // Summary = city name
        Map city = (Map) response.get("city");
        String summary = city != null ? city.get("name").toString() : null;

        // min/max temperatures
        Double min = daily.stream().map(DailyWeatherDTO::temperature).min(Double::compareTo).orElse(null);
        Double max = daily.stream().map(DailyWeatherDTO::temperature).max(Double::compareTo).orElse(null);

        return new WeatherReportDTO(summary, min, max, daily);
    }
}
