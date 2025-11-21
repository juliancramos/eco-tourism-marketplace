package com.marketplace.servicecatalog.service.impl;

import java.time.LocalDateTime;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marketplace.servicecatalog.dto.CreateServiceCategoryDTO;
import com.marketplace.servicecatalog.dto.CreateServiceDTO;
import com.marketplace.servicecatalog.dto.ServiceCategoryDTO;
import com.marketplace.servicecatalog.dto.ServiceDTO;
import com.marketplace.servicecatalog.dto.UpdateServiceDTO;
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

@Service
@AllArgsConstructor
public class ServiceCatalogServiceImpl implements ServiceCatalogService {

    private final ServiceRepository serviceRepository;
    private final ServiceCategoryRepository categoryRepository;
    private final ProviderCacheRepository providerCacheRepository;
    private final ServiceEventPublisher serviceEventPublisher;
    private final ServiceEnrichmentOrchestrator enrichmentOrchestrator;

    @Override
    @Transactional
    public ServiceCategoryDTO getCategory(Long id) {
        var cat = categoryRepository.findById(id).orElseThrow();
        return new ServiceCategoryDTO(cat.getId(), cat.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceDTO getService(Long id) {
        ServiceEntity e = serviceRepository.findById(id).orElseThrow();
        Hibernate.initialize(e.getImages());

        // 🔥 Enriquecer ANTES de mapear
        return enrichmentOrchestrator.enrich(e);
    }

    @Override
    @Transactional
    public Page<ServiceCategoryDTO> listCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(c -> new ServiceCategoryDTO(c.getId(), c.getName()));
    }

    @Override
    @Transactional
    public Page<ServiceDTO> listServices(Long categoryId, String cityCode, Boolean active, Pageable pageable) {
        Page<ServiceEntity> page;

        if (categoryId != null && active != null) {
            page = serviceRepository.findByCategoryIdAndActive(categoryId, active, pageable);

        } else if (cityCode != null && active != null) {
            page = serviceRepository.findByCityCodeAndActive(cityCode, active, pageable);

        } else if (active != null) {
            page = serviceRepository.findByActive(active, pageable);

        } else {
            page = serviceRepository.findAll(pageable);
        }

        return page.map(ServiceMapper::toDto);
    }

    @Override
    public ServiceCategoryDTO createCategory(CreateServiceCategoryDTO dto) {
        var c = new ServiceCategory();
        c.setName(dto.name());
        c = categoryRepository.save(c);
        return new ServiceCategoryDTO(c.getId(), c.getName());
    }

    @Override
    public ServiceDTO create(CreateServiceDTO dto) {

        // validar provider
        if (!providerCacheRepository.existsById(dto.providerId())) {
            throw new EntityNotFoundException("Provider not found with id: " + dto.providerId());
        }

        // Crear entidad
        ServiceEntity e = new ServiceEntity();
        ServiceMapper.applyCreate(e, dto, LocalDateTime.now());
        e = serviceRepository.save(e);

        // Publicar evento
        serviceEventPublisher.publishServiceCreated(
            new ServiceCreatedEvent(
                e.getId(),
                e.getTitle(),
                e.getActive() ? "ACTIVE" : "INACTIVE"
            )
        );

        // 🔥 Enriquecer ANTES de devolver
        return enrichmentOrchestrator.enrich(e);
    }

    @Override
    public ServiceDTO update(UpdateServiceDTO dto) {

        ServiceEntity e = serviceRepository.findById(dto.id()).orElseThrow();

        ServiceMapper.applyUpdate(e, dto);
        e = serviceRepository.save(e);

        // 🔥 Enriquecer ANTES de devolver
        return enrichmentOrchestrator.enrich(e);
    }

    @Override
    public void delete(Long id) {
        serviceRepository.deleteById(id);
    }

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
