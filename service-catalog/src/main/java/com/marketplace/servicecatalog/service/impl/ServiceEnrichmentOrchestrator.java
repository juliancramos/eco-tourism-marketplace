package com.marketplace.servicecatalog.service.impl;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.servicecatalog.dto.MapsInfoDTO;
import com.marketplace.servicecatalog.dto.CountryInfoDTO;
import com.marketplace.servicecatalog.dto.WeatherReportDTO;
import com.marketplace.servicecatalog.dto.ServiceDTO;
import com.marketplace.servicecatalog.mapper.ServiceMapper;
import com.marketplace.servicecatalog.model.ServiceEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceEnrichmentOrchestrator {

    private final GoogleMapsClient mapsClient;       // ahora usa Nominatim + OSRM
    private final RestCountriesClient countriesClient;
    private final WeatherApiClient weatherClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ServiceDTO enrich(ServiceEntity entity) {

        // ==============================
        // COUNTRY INFO
        // ==============================
        try {
            CountryInfoDTO country = countriesClient.getCountry(entity.getCountryCode());
            saveCountryInfoJson(entity, country);
        } catch (Exception ignored) {}

        // ==============================
        // MAPS INFO (geocoding y rutas)
        // ==============================
        try {
            MapsInfoDTO maps = null;

            // Alojamiento → geocoding
            if (entity.getCategoryId() == 1 && entity.getAddress() != null) {
                maps = mapsClient.geocode(entity.getAddress());
                if (maps != null) {
                    entity.setLatitude(maps.latitude());
                    entity.setLongitude(maps.longitude());
                }
            }

            // Transporte → rutas
            if (entity.getCategoryId() == 2 &&
                entity.getRouteOrigin() != null &&
                entity.getRouteDestination() != null) {

                maps = mapsClient.getRoute(
                        entity.getRouteOrigin(),
                        entity.getRouteDestination()
                );
            }

        } catch (Exception ignored) {}

        // ==============================
        // WEATHER
        // ==============================
        try {
            WeatherReportDTO weather =
                    weatherClient.getWeather(entity.getLatitude(), entity.getLongitude());

            saveWeatherJson(entity, weather);
        } catch (Exception ignored) {}

        return ServiceMapper.toDto(entity);
    }

    private void saveCountryInfoJson(ServiceEntity entity, CountryInfoDTO dto) {
        if (dto == null) return;
        try {
            entity.setCountryInfo(objectMapper.writeValueAsString(dto));
        } catch (JsonProcessingException ignored) {}
    }

    private void saveWeatherJson(ServiceEntity entity, WeatherReportDTO dto) {
        if (dto == null) return;
        try {
            entity.setWeatherInfo(objectMapper.writeValueAsString(dto));
        } catch (JsonProcessingException ignored) {}
    }
}
