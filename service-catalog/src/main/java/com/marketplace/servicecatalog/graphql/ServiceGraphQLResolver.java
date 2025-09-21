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
    public List<ServiceCategoryDTO> allCategories() {
        Page<ServiceCategoryDTO> page = serviceCatalogService.listCategories(Pageable.unpaged());
        return page.getContent();
    }


    @QueryMapping
    public ServiceCategoryDTO categoryById(@Argument Long id) {
        return serviceCatalogService.getCategory(id);
    }

}
