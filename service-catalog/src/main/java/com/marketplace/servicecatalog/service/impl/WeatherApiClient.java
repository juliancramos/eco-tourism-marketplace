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

import reactor.core.publisher.Mono;

@Service
public class WeatherApiClient {

    @Value("${weather.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.openweathermap.org")
            .build();

    public Mono<WeatherReportDTO> getWeather(Double lat, Double lon) {

        if (lat == null || lon == null) {
            return Mono.empty();
        }

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/2.5/forecast")
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(response -> {
                    Object listObj = response.get("list");
                    if (!(listObj instanceof List<?> list)) {
                        return Mono.empty();
                    }

                    List<DailyWeatherDTO> daily = list.stream()
                            .map(item -> {
                                Map<?, ?> entry = (Map<?, ?>) item;

                                Long ts = ((Number) entry.get("dt")).longValue();
                                LocalDateTime date = LocalDateTime.ofInstant(
                                        Instant.ofEpochSecond(ts),
                                        ZoneId.systemDefault()
                                );

                                Map<?, ?> main = (Map<?, ?>) entry.get("main");
                                Double temp = ((Number) main.get("temp")).doubleValue();

                                List<?> weatherList = (List<?>) entry.get("weather");
                                Map<?, ?> weather = (Map<?, ?>) weatherList.get(0);
                                String condition = weather.get("description").toString();

                                return new DailyWeatherDTO(date, temp, condition);
                            })
                            .collect(Collectors.toList());

                    Map<?, ?> city = (Map<?, ?>) response.get("city");
                    String summary = city != null ? city.get("name").toString() : null;

                    Double min = daily.stream()
                            .map(DailyWeatherDTO::temperature)
                            .min(Double::compareTo)
                            .orElse(null);

                    Double max = daily.stream()
                            .map(DailyWeatherDTO::temperature)
                            .max(Double::compareTo)
                            .orElse(null);

                    return Mono.just(new WeatherReportDTO(summary, min, max, daily));
                });
    }
}
