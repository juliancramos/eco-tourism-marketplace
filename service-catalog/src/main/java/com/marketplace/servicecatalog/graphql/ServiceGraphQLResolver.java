package com.marketplace.servicecatalog.graphql;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.marketplace.servicecatalog.dto.ServiceDTO;
import com.marketplace.servicecatalog.dto.ServiceCategoryDTO;
import com.marketplace.servicecatalog.service.ServiceCatalogService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ServiceGraphQLResolver {

    private final ServiceCatalogService serviceCatalogService;

    @QueryMapping
    public String hello() {
        return "Hola GraphQL";
    }

    // =============================
    // 🔥 OBTENER UN SERVICIO POR ID
    // =============================
    @QueryMapping
    public ServiceDTO serviceById(@Argument Long id) {
        // GraphQL NO es reactivo → se usa block()
        return serviceCatalogService
                .getService(id)
                .blockOptional()
                .orElse(null);
    }

    // =============================
    // 🔥 LISTAR TODOS LOS SERVICIOS
    // =============================
    @QueryMapping
    @Transactional
    public List<ServiceDTO> allServices() {
        Page<ServiceDTO> page = serviceCatalogService
                .listServices(null, null, null, Pageable.unpaged());
        return page.getContent();
    }

    // =============================
    // 🔥 FILTRAR POR CATEGORÍA
    // =============================
    @QueryMapping
    @Transactional
    public List<ServiceDTO> servicesByCategory(@Argument Long categoryId) {
        return serviceCatalogService
                .listServices(categoryId, null, null, Pageable.unpaged())
                .getContent();
    }

    // =============================
    // 🔥 FILTRAR POR PROVIDER
    // =============================
    @QueryMapping
    @Transactional
    public List<ServiceDTO> servicesByProvider(@Argument Long providerId) {
        return serviceCatalogService
                .listServicesByProvider(providerId, Pageable.unpaged())
                .getContent();
    }

    // =============================
    // 🔥 FILTRAR POR CIUDAD
    // =============================
    @QueryMapping
    @Transactional
    public List<ServiceDTO> servicesByCity(@Argument String cityCode) {
        return serviceCatalogService
                .listServices(null, cityCode, null, Pageable.unpaged())
                .getContent();
    }

    // =============================
    // 🔥 FILTRAR POR PAÍS
    // =============================
    @QueryMapping
    @Transactional
    public List<ServiceDTO> servicesByCountry(@Argument String countryCode) {
        return serviceCatalogService
                .listServicesByCountry(countryCode, Pageable.unpaged())
                .getContent();
    }

    // =============================
    // 🔍 BUSCAR POR TÍTULO
    // =============================
    @QueryMapping
    @Transactional
    public List<ServiceDTO> searchServicesByTitle(@Argument String keyword) {
        return serviceCatalogService
                .searchServicesByTitle(keyword, Pageable.unpaged())
                .getContent();
    }

    // =============================
    // 🔥 FILTRAR POR PRECIO
    // =============================
    @QueryMapping
    @Transactional
    public List<ServiceDTO> servicesByPriceRange(
            @Argument Double minPrice,
            @Argument Double maxPrice
    ) {
        return serviceCatalogService
                .findByPriceRange(minPrice, maxPrice, Pageable.unpaged())
                .getContent();
    }

    // =============================
    // 🔥 SOLO ACTIVOS
    // =============================
    @QueryMapping
    @Transactional
    public List<ServiceDTO> activeServices() {
        return serviceCatalogService
                .findActiveServices(Pageable.unpaged())
                .getContent();
    }

    // =============================
    // 🔥 CATEGORÍAS
    // =============================
    @QueryMapping
    @Transactional
    public List<ServiceCategoryDTO> allCategories() {
        return serviceCatalogService
                .listCategories(Pageable.unpaged())
                .getContent();
    }

    @QueryMapping
    public ServiceCategoryDTO categoryById(@Argument Long id) {
        return serviceCatalogService.getCategory(id);
    }
}
