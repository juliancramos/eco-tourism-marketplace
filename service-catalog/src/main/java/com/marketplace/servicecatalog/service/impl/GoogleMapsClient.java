package com.marketplace.servicecatalog.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.marketplace.servicecatalog.dto.MapsInfoDTO;

@Service
public class GoogleMapsClient {

    private final WebClient webClient = WebClient.create();

    /**
     * Geocoding GRATIS usando OpenStreetMap (Nominatim)
     */
    public MapsInfoDTO geocode(String address) {
        String url = "https://nominatim.openstreetmap.org/search";

        List response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(url)
                        .queryParam("q", address)
                        .queryParam("format", "json")
                        .queryParam("limit", "1")
                        .build())
                .header("User-Agent", "EcoTourismMarketplace/1.0") // requerido por Nominatim
                .retrieve()
                .bodyToMono(List.class)
                .block();

        if (response == null || response.isEmpty()) return null;

        Map data = (Map) response.get(0);

        Double lat = Double.parseDouble(data.get("lat").toString());
        Double lon = Double.parseDouble(data.get("lon").toString());

        return new MapsInfoDTO(lat, lon, null);
    }

    /**
     * Rutas GRATIS usando OSRM (Open Source Routing Machine)
     */
    public MapsInfoDTO getRoute(String originAddress, String destinationAddress) {

        // Primero geocodificamos origen y destino
        MapsInfoDTO origin = geocode(originAddress);
        MapsInfoDTO destination = geocode(destinationAddress);

        if (origin == null || destination == null) return null;

        String url = String.format(
            "https://router.project-osrm.org/route/v1/driving/%f,%f;%f,%f",
            origin.longitude(), origin.latitude(),
            destination.longitude(), destination.latitude()
        );

        Map response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(url)
                        .queryParam("overview", "full")
                        .queryParam("geometries", "polyline")
                        .build())
                .header("User-Agent", "EcoTourismMarketplace/1.0")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null || response.get("routes") == null) return null;

        List routes = (List) response.get("routes");
        if (routes.isEmpty()) return null;

        Map route = (Map) routes.get(0);

        String polyline = route.get("geometry").toString();

        return new MapsInfoDTO(null, null, polyline);
    }
}
