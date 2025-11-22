package com.marketplace.servicecatalog.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.marketplace.servicecatalog.dto.CountryInfoDTO;
import com.marketplace.servicecatalog.dto.MapsInfoDTO;
import com.marketplace.servicecatalog.dto.ServiceDTO;
import com.marketplace.servicecatalog.dto.WeatherReportDTO;
import com.marketplace.servicecatalog.mapper.ServiceMapper;
import com.marketplace.servicecatalog.model.ServiceEntity;
import com.marketplace.servicecatalog.repository.ServiceCategoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceEnrichmentOrchestrator {

    private final GoogleMapsClient mapsClient;
    private final RestCountriesClient countriesClient;
    private final WeatherApiClient weatherClient;
    private final ServiceCategoryRepository categoryRepo;

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public Mono<ServiceDTO> enrich(ServiceEntity entity) {

        log.warn("🔥 [ENRICH] Iniciando enriquecimiento para serviceId={}", entity.getId());

        // 1️⃣ Categoría desde DB
        Mono<String> categoryMono = Mono.fromCallable(() ->
                        categoryRepo.findById(entity.getCategoryId())
                                .orElseThrow(() -> new RuntimeException(
                                        "Category not found with id: " + entity.getCategoryId()))
                                .getName()
                                .trim()
                                .toLowerCase())
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(cat -> log.warn("📦 [CAT] Categoria='{}'", cat))
                .onErrorResume(ex -> {
                    log.error("❌ [CAT] Error obteniendo categoría", ex);
                    return Mono.empty();
                });

        // 2️⃣ Country info
        Mono<CountryInfoDTO> countryMono = countriesClient.getCountry(entity.getCountryCode())
                .doOnSubscribe(s -> log.warn("🌍 [COUNTRY] Consultando país '{}'", entity.getCountryCode()))
                .doOnNext(country -> {
                    try {
                        entity.setCountryInfo(mapper.writeValueAsString(country));
                    } catch (JsonProcessingException e) {
                        log.error("❌ [COUNTRY] Error serializando JSON país", e);
                    }
                })
                .onErrorResume(ex -> {
                    log.error("❌ [COUNTRY] Error consultando país", ex);
                    return Mono.empty();
                });

        // 3️⃣ Maps según categoría
        Mono<MapsInfoDTO> mapsMono = categoryMono.flatMap(cat -> {
                    if ("alojamiento".equals(cat) && entity.getAddress() != null) {
                        log.warn("📍 [GEO] Geocoding '{}'", entity.getAddress());
                        return mapsClient.geocode(entity.getAddress());
                    }
                    if ("transporte".equals(cat) && entity.getRouteOrigin() != null && entity.getRouteDestination() != null) {
                        log.warn("🛣 [ROUTE] {} -> {}", entity.getRouteOrigin(), entity.getRouteDestination());
                        return mapsClient.getRoute(entity.getRouteOrigin(), entity.getRouteDestination());
                    }
                    return Mono.empty();
                })
                .doOnNext(loc -> {
                    if (loc.latitude() != null) entity.setLatitude(loc.latitude());
                    if (loc.longitude() != null) entity.setLongitude(loc.longitude());
                })
                .onErrorResume(ex -> {
                    log.error("❌ [MAPS] Error geocoding/rutas", ex);
                    return Mono.empty();
                });

        // 4️⃣ Weather
        Mono<WeatherReportDTO> weatherMono = mapsMono.flatMap(loc -> {
                    if (loc.latitude() == null || loc.longitude() == null) {
                        log.warn("⛅ [WEATHER] Sin coordenadas");
                        return Mono.empty();
                    }
                    log.warn("⛅ [WEATHER] Clima lat={}, lon={}", loc.latitude(), loc.longitude());
                    return weatherClient.getWeather(loc.latitude(), loc.longitude());
                })
                .doOnNext(w -> {
                    try {
                        entity.setWeatherInfo(mapper.writeValueAsString(w));
                    } catch (JsonProcessingException e) {
                        log.error("❌ [WEATHER] Error serializando JSON clima", e);
                    }
                })
                .onErrorResume(ex -> {
                    log.error("❌ [WEATHER] Error obteniendo clima", ex);
                    return Mono.empty();
                });

        // 5️⃣ ZIP seguro y sin null-problems
        return Mono.zip(
                        countryMono.switchIfEmpty(Mono.empty()),
                        mapsMono.switchIfEmpty(Mono.empty()),
                        weatherMono.switchIfEmpty(Mono.empty())
                )
                .flatMap(tuple -> {
                    CountryInfoDTO country = tuple.getT1();
                    MapsInfoDTO maps = tuple.getT2();
                    WeatherReportDTO weather = tuple.getT3();

                    log.warn("✨ [DONE] Enriquecimiento completado serviceId={}", entity.getId());

                    return Mono.fromCallable(() ->
                                    ServiceMapper.toDto(entity, country, maps, weather))
                            .subscribeOn(Schedulers.boundedElastic());
                });
    }
}
