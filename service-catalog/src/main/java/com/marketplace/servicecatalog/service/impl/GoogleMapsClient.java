package com.marketplace.servicecatalog.service.impl;

import com.marketplace.servicecatalog.dto.MapsInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleMapsClient {

    private final WebClient nominatimClient = WebClient.builder()
            .baseUrl("https://nominatim.openstreetmap.org")
            .defaultHeader("User-Agent", "EcoTourism-App/1.0")
            .build();

    private final WebClient osrmClient = WebClient.builder()
            .baseUrl("http://router.project-osrm.org")
            .build();


    // ============================================================
    // 1) GEOCODING (Nominatim)
    // ============================================================
    public Mono<MapsInfoDTO> geocode(String address) {
        return nominatimClient.get()
                .uri(uri -> uri
                        .path("/search")
                        .queryParam("q", address)
                        .queryParam("format", "json")
                        .queryParam("limit", "1")
                        .build())
                .retrieve()
                .bodyToFlux(OSMNominatimResponse.class)
                .next()
                .map(res -> {
                    log.warn("📍 [NOMINATIM] '{}' => lat={}, lon={}", address, res.lat(), res.lon());
                    return new MapsInfoDTO(
                            res.lat(),
                            res.lon(),
                            null
                    );
                })
                .onErrorResume(ex -> {
                    log.error("❌ [NOMINATIM] Error geocoding '{}': {}", address, ex.getMessage());
                    return Mono.empty();
                });
    }


    // ============================================================
    // 2) RUTAS OSRM — Correcto: primero geocoding, luego ruta con coords
    // ============================================================
    public Mono<MapsInfoDTO> getRoute(String origin, String destination) {

        // 1️⃣ Geocoding origen
        Mono<MapsInfoDTO> originGeo = geocode(origin)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("No se pudo geocodificar origen: " + origin)
                ));

        // 2️⃣ Geocoding destino
        Mono<MapsInfoDTO> destGeo = geocode(destination)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("No se pudo geocodificar destino: " + destination)
                ));

        // 3️⃣ Combinar origen + destino => llamar OSRM con coordenadas reales
        return Mono.zip(originGeo, destGeo)
                .flatMap(tuple -> {

                    MapsInfoDTO o = tuple.getT1();
                    MapsInfoDTO d = tuple.getT2();

                    // Coordenadas OSRM deben ser LON,LAT
                    String oCoord = o.longitude() + "," + o.latitude();
                    String dCoord = d.longitude() + "," + d.latitude();

                    log.warn("🛣 [OSRM] Ruta coords: {} → {}", oCoord, dCoord);

                    return osrmClient.get()
                            .uri(uri -> uri
                                    .path("/route/v1/driving/{o};{d}")
                                    .queryParam("overview", "full")
                                    .queryParam("geometries", "polyline")
                                    .build(oCoord, dCoord)
                            )
                            .retrieve()
                            .bodyToMono(OSRMRouteResponse.class)
                            .map(route -> {
                                String polyline = null;

                                if (route.routes() != null && !route.routes().isEmpty()) {
                                    polyline = route.routes().get(0).geometry();
                                }

                                // Retornamos MapsInfo con lat/lon del DESTINO (final)
                                return new MapsInfoDTO(
                                        d.latitude(),
                                        d.longitude(),
                                        polyline
                                );
                            });
                })
                .onErrorResume(ex -> {
                    log.error("❌ [MAPS] Error obteniendo ruta {} → {}: {}", 
                            origin, destination, ex.getMessage());
                    return Mono.empty();
                });
    }


    // ============================================================
    // DTOs internos para JSON
    // ============================================================
    record OSMNominatimResponse(
            Double lat,
            Double lon
    ) {}

    record OSRMRouteResponse(
            java.util.List<Route> routes
    ) {
        record Route(
                String geometry
        ) {}
    }
}
