package com.marketplace.servicecatalog.service.impl;

import java.time.LocalDateTime;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marketplace.servicecatalog.dto.*;
import com.marketplace.servicecatalog.mapper.ServiceMapper;
import com.marketplace.servicecatalog.model.ServiceCategory;
import com.marketplace.servicecatalog.model.ServiceEntity;
import com.marketplace.servicecatalog.queue.ServiceCreatedEvent;
import com.marketplace.servicecatalog.queue.ServiceEventPublisher;
import com.marketplace.servicecatalog.repository.ProviderCacheRepository;
import com.marketplace.servicecatalog.repository.ServiceCategoryRepository;
import com.marketplace.servicecatalog.repository.ServiceRepository;
import com.marketplace.servicecatalog.service.ServiceCatalogService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@AllArgsConstructor
public class ServiceCatalogServiceImpl implements ServiceCatalogService {

    private final ServiceRepository serviceRepository;
    private final ServiceCategoryRepository categoryRepository;
    private final ProviderCacheRepository providerCacheRepository;
    private final ServiceEventPublisher serviceEventPublisher;
    private final ServiceEnrichmentOrchestrator enrichment;

    @Override
    @Transactional(readOnly = true)
    public Mono<ServiceDTO> getService(Long id) {

        return Mono.fromCallable(() -> {
                    ServiceEntity e = serviceRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Service not found " + id));

                    Hibernate.initialize(e.getImages());

                    return e;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(enrichment::enrich);  
    }


    // ================================================================
    // CATEGORY
    // ================================================================
    @Override
    @Transactional
    public ServiceCategoryDTO getCategory(Long id) {
        var c = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found " + id));
        return new ServiceCategoryDTO(c.getId(), c.getName());
    }

    @Override
    @Transactional
    public Page<ServiceCategoryDTO> listCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(c -> new ServiceCategoryDTO(c.getId(), c.getName()));
    }

    // ================================================================
    // LIST SERVICES
    // ================================================================
    @Override
    @Transactional
    public Page<ServiceDTO> listServices(Long categoryId, String cityCode, Boolean active, Pageable pageable) {

        Page<ServiceEntity> page;

        if (categoryId != null && active != null)
            page = serviceRepository.findByCategoryIdAndActive(categoryId, active, pageable);
        else if (cityCode != null && active != null)
            page = serviceRepository.findByCityCodeAndActive(cityCode, active, pageable);
        else if (active != null)
            page = serviceRepository.findByActive(active, pageable);
        else
            page = serviceRepository.findAll(pageable);

        return page.map(ServiceMapper::toDto); // listado simple
    }

    @Override
    public ServiceCategoryDTO createCategory(CreateServiceCategoryDTO dto) {
        var c = new ServiceCategory();
        c.setName(dto.name());
        c = categoryRepository.save(c);

        return new ServiceCategoryDTO(c.getId(), c.getName());
    }

    // ================================================================
    // CREATE
    // ================================================================
    @Override
    @Transactional
    public Mono<ServiceDTO> create(CreateServiceDTO dto) {

        return Mono.fromCallable(() -> {

                    if (!providerCacheRepository.existsById(dto.providerId())) {
                        throw new EntityNotFoundException("Provider not found " + dto.providerId());
                    }

                    ServiceEntity e = new ServiceEntity();
                    ServiceMapper.applyCreate(e, dto, LocalDateTime.now());
                    e = serviceRepository.save(e);

                    serviceEventPublisher.publishServiceCreated(
                            new ServiceCreatedEvent(
                                    e.getId(),
                                    e.getTitle(),
                                    e.getActive() ? "ACTIVE" : "INACTIVE"
                            )
                    );

                    return e;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(enrichment::enrich);  // enrichment recibe entity
    }

    // ================================================================
    // UPDATE
    // ================================================================
    @Override
    @Transactional
    public Mono<ServiceDTO> update(UpdateServiceDTO dto) {

        return Mono.fromCallable(() -> {

                    ServiceEntity e = serviceRepository.findById(dto.id())
                            .orElseThrow(() -> new EntityNotFoundException("Service not found " + dto.id()));

                    ServiceMapper.applyUpdate(e, dto);
                    return serviceRepository.save(e);

                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(enrichment::enrich);
    }

    // ================================================================
    // DELETE
    // ================================================================
    @Override
    @Transactional
    public void delete(Long id) {
        serviceRepository.deleteById(id);
    }

    // ================================================================
    // FILTERS
    // ================================================================
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceDTO> listServicesByProvider(Long providerId, Pageable pageable) {
        return serviceRepository.findByProviderId(providerId, pageable)
                .map(ServiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceDTO> listServicesByCountry(String countryCode, Pageable pageable) {
        return serviceRepository.findByCountryCode(countryCode, pageable)
                .map(ServiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceDTO> searchServicesByTitle(String keyword, Pageable pageable) {
        return serviceRepository.findByTitleContainingIgnoreCase(keyword, pageable)
                .map(ServiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceDTO> findByPriceRange(Double minPrice, Double maxPrice, Pageable pageable) {
        return serviceRepository.findByPriceBetween(minPrice, maxPrice, pageable)
                .map(ServiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceDTO> findActiveServices(Pageable pageable) {
        return serviceRepository.findByActive(true, pageable)
                .map(ServiceMapper::toDto);
    }
}
