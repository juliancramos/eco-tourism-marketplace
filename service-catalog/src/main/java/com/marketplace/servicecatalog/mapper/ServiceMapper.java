package com.marketplace.servicecatalog.mapper;

import java.time.Instant;
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
    List<ServiceImageDTO> imgs;
      imgs = e.getImages() == null ? List.of() :
              e.getImages().stream()
                      .map(i -> new ServiceImageDTO(i.getId(), i.getUrl(), i.getPosition()))
                      .toList();

    Boolean isActive = Boolean.TRUE.equals(e.getActive());
    return new ServiceDTO(
        e.getId(),
        e.getProviderId(),
        e.getCategoryId(),
        e.getTitle(),
        e.getDescription(),
        e.getPrice(),
        e.getCurrency(),
        isActive,
        e.getCountryCode(),
        e.getCityCode(),
        e.getAddress(),
        e.getCreationDate(),
        imgs
    );
  }

  public static void applyCreate(ServiceEntity e, CreateServiceDTO dto, Long newId, Instant now) {
    e.setId(newId);
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
    e.setImages(new ArrayList<>());
    if (dto.images() != null) {
      int idx = 0;
      for (ServiceImageDTO d : dto.images()) {
        ServiceImage i = new ServiceImage();
        i.setId(newId * 1000 + idx++); // posibilidad de generación de id
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

    // Reemplazo de imágenes
    e.getImages().clear();
    if (dto.images() != null) {
      int idx = 0;
      for (ServiceImageDTO d : dto.images()) {
        ServiceImage i = new ServiceImage();
        i.setId(dto.id() * 1000 + idx++);
        i.setService(e);
        i.setUrl(d.url());
        i.setPosition(d.position());
        e.getImages().add(i);
      }
    }
  }
}