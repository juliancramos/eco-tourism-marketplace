package com.marketplace.servicecatalog.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.servicecatalog.dto.CreateServiceCategoryDTO;
import com.marketplace.servicecatalog.dto.ServiceCategoryDTO;
import com.marketplace.servicecatalog.service.ServiceCatalogService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class ServiceCategoryController {

    private final ServiceCatalogService service;

    @GetMapping("/{id}")
    public ServiceCategoryDTO get(@PathVariable Long id) {
        return service.getCategory(id);
    }

    @GetMapping
    public Page<ServiceCategoryDTO> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return service.listCategories(PageRequest.of(page, size));
    }

    @PostMapping
    public ServiceCategoryDTO create(@RequestBody CreateServiceCategoryDTO dto) {
        return service.createCategory(dto);
    }

}
