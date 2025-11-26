package com.marketplace.servicecatalog.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.marketplace.servicecatalog.dto.*;
import com.marketplace.servicecatalog.model.ServiceEntity;
import com.marketplace.servicecatalog.model.ServiceImage;

public class ServiceMapper {

    // ============================================================
    // 1) DTO SIMPLE (USADO EN LISTADOS)
    // ============================================================
    public static ServiceDTO toDto(ServiceEntity e) {
        return new ServiceDTO(
                e.getId(),
                e.getProviderId(),
                e.getCategoryId(),
                e.getTitle(),
                e.getDescription(),
                e.getPrice(),
                e.getCurrency(),
                e.getActive(),
                e.getCountryCode(),
                e.getCityCode(),
                e.getAddress(),
                e.getCreationDate(),
                e.getStartDate(),
                e.getEndDate(),
                e.getTransportType(),
                e.getRouteOrigin(),
                e.getRouteDestination(),
                e.getLatitude(),
                e.getLongitude(),
                e.getImages().stream()
                        .map(i -> new ServiceImageDTO(i.getUrl(), i.getPosition()))
                        .toList(),

                // ❗ version simple NO incluye enrichment
                null,
                null,
                null
        );
    }

    // ============================================================
    // 2) DTO COMPLETO (ENRICHED)
    // ============================================================
    public static ServiceDTO toDto(ServiceEntity e,
                                   CountryInfoDTO country,
                                   MapsInfoDTO maps,
                                   WeatherReportDTO weather) {

        return new ServiceDTO(
                e.getId(),
                e.getProviderId(),
                e.getCategoryId(),
                e.getTitle(),
                e.getDescription(),
                e.getPrice(),
                e.getCurrency(),
                e.getActive(),
                e.getCountryCode(),
                e.getCityCode(),
                e.getAddress(),
                e.getCreationDate(),
                e.getStartDate(),
                e.getEndDate(),
                e.getTransportType(),
                e.getRouteOrigin(),
                e.getRouteDestination(),
                e.getLatitude(),
                e.getLongitude(),
                e.getImages().stream()
                        .map(i -> new ServiceImageDTO(i.getUrl(), i.getPosition()))
                        .toList(),

                // 🔥 enriched fields
                country,
                maps,
                weather
        );
    }

    // ============================================================
    // CREATE
    // ============================================================
    public static void applyCreate(ServiceEntity e, CreateServiceDTO dto, LocalDateTime now) {
        e.setProviderId(dto.providerId());
        e.setCategoryId(dto.categoryId());
        e.setTitle(dto.title());
        e.setDescription(dto.description());
        e.setPrice(dto.price());
        e.setCurrency(dto.currency());
        e.setActive(dto.active());
        e.setCountryCode(dto.countryCode());
        e.setCityCode(dto.cityCode());
        e.setAddress(dto.address());
        e.setCreationDate(now);

        e.setStartDate(dto.startDate());
        e.setEndDate(dto.endDate());
        e.setTransportType(dto.transportType());
        e.setRouteOrigin(dto.routeOrigin());
        e.setRouteDestination(dto.routeDestination());
        e.setLatitude(dto.latitude());
        e.setLongitude(dto.longitude());

        e.setImages(new ArrayList<>());
        if (dto.images() != null) {
            dto.images().forEach(img -> {
                ServiceImage i = new ServiceImage();
                i.setService(e);
                i.setUrl(img.url());
                i.setPosition(img.position());
                e.getImages().add(i);
            });
        }
    }

    // ============================================================
    // UPDATE
    // ============================================================
    public static void applyUpdate(ServiceEntity e, UpdateServiceDTO dto) {
        e.setProviderId(dto.providerId());
        e.setCategoryId(dto.categoryId());
        e.setTitle(dto.title());
        e.setDescription(dto.description());
        e.setPrice(dto.price());
        e.setCurrency(dto.currency());
        e.setActive(dto.active());
        e.setCountryCode(dto.countryCode());
        e.setCityCode(dto.cityCode());
        e.setAddress(dto.address());

        e.setStartDate(dto.startDate());
        e.setEndDate(dto.endDate());
        e.setTransportType(dto.transportType());
        e.setRouteOrigin(dto.routeOrigin());
        e.setRouteDestination(dto.routeDestination());
        e.setLatitude(dto.latitude());
        e.setLongitude(dto.longitude());

        e.getImages().clear();
        if (dto.images() != null) {
            dto.images().forEach(img -> {
                ServiceImage i = new ServiceImage();
                i.setService(e);
                i.setUrl(img.url());
                i.setPosition(img.position());
                e.getImages().add(i);
            });
        }
    }
}
