package com.marketplace.servicecatalog.dto;


public record MapsInfoDTO(
    Double latitude,
    Double longitude,
    String routePolyline
) {}

