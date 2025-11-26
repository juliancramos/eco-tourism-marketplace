package com.marketplace.servicecatalog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.marketplace.servicecatalog.dto.CreateServiceCategoryDTO;
import com.marketplace.servicecatalog.dto.CreateServiceDTO;
import com.marketplace.servicecatalog.dto.ServiceCategoryDTO;
import com.marketplace.servicecatalog.dto.ServiceDTO;
import com.marketplace.servicecatalog.dto.UpdateServiceDTO;

import reactor.core.publisher.Mono;

public interface  ServiceCatalogService {
    ServiceCategoryDTO getCategory(Long id);
     Mono<ServiceDTO> getService(Long id);

    Page<ServiceCategoryDTO> listCategories(Pageable pageable);
    Page<ServiceDTO> listServices(Long categoryId, String cityCode, Boolean active, Pageable pageable);
    ServiceCategoryDTO createCategory(CreateServiceCategoryDTO name);

    Mono<ServiceDTO> create(CreateServiceDTO dto);
    Mono<ServiceDTO> update(UpdateServiceDTO dto);
    void delete(Long id);

    Page<ServiceDTO> listServicesByProvider(Long providerId, Pageable pageable);

    Page<ServiceDTO> listServicesByCountry(String countryCode, Pageable pageable);

    Page<ServiceDTO> searchServicesByTitle(String keyword, Pageable pageable);

    Page<ServiceDTO> findByPriceRange(Double minPrice, Double maxPrice, Pageable pageable);

    Page<ServiceDTO> findActiveServices(Pageable pageable);
}
