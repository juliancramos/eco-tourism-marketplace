package com.marketplace.servicecatalog.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.servicecatalog.dto.CreateServiceDTO;
import com.marketplace.servicecatalog.dto.ServiceDTO;
import com.marketplace.servicecatalog.dto.UpdateServiceDTO;
import com.marketplace.servicecatalog.service.ServiceCatalogService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/services")
@AllArgsConstructor
public class ServiceController {

    private final ServiceCatalogService svc;

    @GetMapping("/{id}")
    public ServiceDTO get(@PathVariable Long id) {
        return svc.getService(id);
    }

    @GetMapping
    public Page<ServiceDTO> list(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String cityCode,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return svc.listServices(categoryId, cityCode, active, PageRequest.of(page, size));
    }

    @PostMapping
    public ServiceDTO create(@Valid @RequestBody CreateServiceDTO dto) {
        return svc.create(dto);
    }

    @PutMapping("/{id}")
    public ServiceDTO update(@PathVariable Long id, @Valid @RequestBody UpdateServiceDTO dto) {
        return svc.update(new UpdateServiceDTO(
                id, dto.providerId(), dto.categoryId(), dto.title(), dto.description(),
                dto.price(), dto.currency(), dto.active(), dto.countryCode(), dto.cityCode(),
                dto.address(), dto.images()));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        svc.delete(id);
    }

}
