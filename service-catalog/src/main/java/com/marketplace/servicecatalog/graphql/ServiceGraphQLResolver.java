package com.marketplace.servicecatalog.graphql;

import org.springframework.stereotype.Controller;

import com.marketplace.servicecatalog.dto.ServiceDTO;
import com.marketplace.servicecatalog.mapper.ServiceMapper;
import com.marketplace.servicecatalog.model.ServiceCategory;
import com.marketplace.servicecatalog.repository.ServiceCategoryRepository;
import com.marketplace.servicecatalog.repository.ServiceRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;

@Controller
public class ServiceGraphQLResolver {
    private final ServiceRepository serviceRepository;
    private final ServiceCategoryRepository categoryRepository;

    public ServiceGraphQLResolver(ServiceRepository serviceRepository,
                                  ServiceCategoryRepository categoryRepository) {
        this.serviceRepository = serviceRepository;
        this.categoryRepository = categoryRepository;
    }

    @QueryMapping
    public String hello() {
        System.out.println("Resolver hello ejecutado");
        return "Hola GraphQL";
    }

    @QueryMapping
    @Transactional
    public List<ServiceDTO> allServices() {
        List<ServiceDTO> services = serviceRepository.findAll()
                .stream()
                .map(ServiceMapper::toDto)
                .collect(Collectors.toList());
        System.out.println("Servicios encontrados: " + services.size());
        return services;
    }

    @QueryMapping
    public ServiceDTO serviceById(@Argument Long id) {
        return serviceRepository.findById(id)
                .map(ServiceMapper::toDto)
                .orElse(null);
    }

    @QueryMapping
    public List<ServiceCategory> allCategories() {
        return categoryRepository.findAll();
    }

    @QueryMapping
    public ServiceCategory categoryById(@Argument Long id) {
        return categoryRepository.findById(id).orElse(null);
    }
}
