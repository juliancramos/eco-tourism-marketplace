package com.marketplace.servicecatalog.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.marketplace.servicecatalog.dto.CreateServiceDTO;
import com.marketplace.servicecatalog.dto.ServiceDTO;
import com.marketplace.servicecatalog.dto.ServiceImageDTO;
import com.marketplace.servicecatalog.dto.UpdateServiceDTO;
import com.marketplace.servicecatalog.model.ServiceEntity;
import com.marketplace.servicecatalog.model.ServiceImage;

public class ServiceMapper {

    public static ServiceDTO toDto(ServiceEntity e) {

        List<ServiceImageDTO> imgs =
                (e.getImages() == null)
                        ? List.of()
                        : e.getImages()
                           .stream()
                           .map(i -> new ServiceImageDTO(i.getUrl(), i.getPosition()))
                           .toList();

        return new ServiceDTO(
                e.getId(),
                e.getProviderId(),
                e.getCategoryId(),
                e.getTitle(),
                e.getDescription(),
                e.getPrice(),
                e.getCurrency(),
                Boolean.TRUE.equals(e.getActive()),
                e.getCountryCode(),
                e.getCityCode(),
                e.getAddress(),
                e.getCreationDate(),

                // CAMPOS DE ALOJAMIENTO
                e.getStartDate(),
                e.getEndDate(),

                // CAMPOS DE TRANSPORTE
                e.getTransportType(),
                e.getRouteOrigin(),
                e.getRouteDestination(),

                // COORDENADAS
                e.getLatitude(),
                e.getLongitude(),

                imgs
        );
    }


    public static void applyCreate(ServiceEntity e, CreateServiceDTO dto, LocalDateTime now) {

        e.setProviderId(dto.providerId());
        e.setCategoryId(dto.categoryId());
        e.setTitle(dto.title());
        e.setDescription(dto.description());
        e.setPrice(dto.price());
        e.setCurrency(dto.currency());
        e.setActive(Boolean.TRUE.equals(dto.active()));
        e.setCountryCode(dto.countryCode());
        e.setCityCode(dto.cityCode());
        e.setAddress(dto.address());
        e.setCreationDate(now);

        // OPCIONALES ALOJAMIENTO
        e.setStartDate(dto.startDate());
        e.setEndDate(dto.endDate());

        // OPCIONALES TRANSPORTE
        e.setTransportType(dto.transportType());
        e.setRouteOrigin(dto.routeOrigin());
        e.setRouteDestination(dto.routeDestination());

        // OPCIONALES PARA MAPS
        e.setLatitude(dto.latitude());
        e.setLongitude(dto.longitude());

        e.setImages(new ArrayList<>());

        if (dto.images() != null) {
            for (ServiceImageDTO d : dto.images()) {
                ServiceImage i = new ServiceImage();
                i.setService(e);
                i.setUrl(d.url());
                i.setPosition(d.position());
                e.getImages().add(i);
            }
        }
    }

    public static void applyUpdate(ServiceEntity e, UpdateServiceDTO dto) {

        e.setProviderId(dto.providerId());
        e.setCategoryId(dto.categoryId());
        e.setTitle(dto.title());
        e.setDescription(dto.description());
        e.setPrice(dto.price());
        e.setCurrency(dto.currency());
        e.setActive(Boolean.TRUE.equals(dto.active()));
        e.setCountryCode(dto.countryCode());
        e.setCityCode(dto.cityCode());
        e.setAddress(dto.address());

        // OPCIONALES ALOJAMIENTO
        e.setStartDate(dto.startDate());
        e.setEndDate(dto.endDate());

        // OPCIONALES TRANSPORTE
        e.setTransportType(dto.transportType());
        e.setRouteOrigin(dto.routeOrigin());
        e.setRouteDestination(dto.routeDestination());

        // OPCIONALES MAPS
        e.setLatitude(dto.latitude());
        e.setLongitude(dto.longitude());

        e.getImages().clear();

        if (dto.images() != null) {
            for (ServiceImageDTO d : dto.images()) {
                ServiceImage i = new ServiceImage();
                i.setService(e);
                i.setUrl(d.url());
                i.setPosition(d.position());
                e.getImages().add(i);
            }
        }
    }
}
