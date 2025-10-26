package com.marketplace.servicecatalog.graphql;

import org.springframework.stereotype.Controller;

import com.marketplace.servicecatalog.dto.ServiceCategoryDTO;
import com.marketplace.servicecatalog.dto.ServiceDTO;
import com.marketplace.servicecatalog.service.ServiceCatalogService;

import jakarta.transaction.Transactional;

import java.util.List;


import org.springframework.data.domain.Page;    
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;

@Controller
public class ServiceGraphQLResolver {

    private final ServiceCatalogService serviceCatalogService;
    public ServiceGraphQLResolver(ServiceCatalogService serviceCatalogService) {

        this.serviceCatalogService = serviceCatalogService;
    }

    @QueryMapping
    public String hello() {
        System.out.println("Resolver hello ejecutado");
        return "Hola GraphQL";
    }

    @QueryMapping
    @Transactional
    public List<ServiceDTO> allServices() {
        Page<ServiceDTO> page = serviceCatalogService.listServices(null, null, null, Pageable.unpaged());
        List<ServiceDTO> services = page.getContent();

        System.out.println("Servicios encontrados: " + services.size());
        return services;
    }


    @QueryMapping
    public ServiceDTO serviceById(@Argument Long id) {
        return serviceCatalogService.getService(id);
    }

    @QueryMapping
    @Transactional
    public List<ServiceDTO> servicesByCategory(@Argument Long categoryId) {
        return serviceCatalogService.listServices(categoryId, null, null, Pageable.unpaged()).getContent();
    }

    @QueryMapping
    @Transactional
    public List<ServiceDTO> servicesByProvider(@Argument Long providerId) {
        return serviceCatalogService.listServicesByProvider(providerId, Pageable.unpaged()).getContent();
    }

    @QueryMapping
    @Transactional
    public List<ServiceDTO> servicesByCity(@Argument String cityCode) {
        return serviceCatalogService.listServices(null, cityCode, null, Pageable.unpaged()).getContent();
    }

    @QueryMapping
    @Transactional
    public List<ServiceDTO> servicesByCountry(@Argument String countryCode) {
        return serviceCatalogService.listServicesByCountry(countryCode, Pageable.unpaged()).getContent();
    }

    @QueryMapping
    @Transactional
    public List<ServiceDTO> searchServicesByTitle(@Argument String keyword) {
        return serviceCatalogService.searchServicesByTitle(keyword, Pageable.unpaged()).getContent();
    }

    @QueryMapping
    @Transactional
    public List<ServiceDTO> servicesByPriceRange(@Argument Double minPrice, @Argument Double maxPrice) {
        return serviceCatalogService.findByPriceRange(minPrice, maxPrice, Pageable.unpaged()).getContent();
    }

    @QueryMapping
    @Transactional
    public List<ServiceDTO> activeServices() {
        return serviceCatalogService.findActiveServices(Pageable.unpaged()).getContent();
    }

    @QueryMapping
    @Transactional
    public List<ServiceCategoryDTO> allCategories() {
        Page<ServiceCategoryDTO> page = serviceCatalogService.listCategories(Pageable.unpaged());
        return page.getContent();
    }


    @QueryMapping
    public ServiceCategoryDTO categoryById(@Argument Long id) {
        return serviceCatalogService.getCategory(id);
    }

    
}
