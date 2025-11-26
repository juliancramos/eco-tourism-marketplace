package com.marketplace.servicecatalog.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import com.marketplace.servicecatalog.dto.CreateServiceDTO;
import com.marketplace.servicecatalog.dto.ServiceDTO;
import com.marketplace.servicecatalog.dto.UpdateServiceDTO;
import com.marketplace.servicecatalog.service.ServiceCatalogService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/services")
@AllArgsConstructor
public class ServiceController {

    private final ServiceCatalogService svc;

    @GetMapping("/{id}")
    public Mono<ServiceDTO> get(@PathVariable Long id) {
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
    public Mono<ServiceDTO> create(@Valid @RequestBody CreateServiceDTO dto) {
        return svc.create(dto);
    }

    @PutMapping("/{id}")
    public Mono<ServiceDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateServiceDTO dto) {

        UpdateServiceDTO fixedDto = new UpdateServiceDTO(
                id,
                dto.providerId(),
                dto.categoryId(),
                dto.title(),
                dto.description(),
                dto.price(),
                dto.currency(),
                dto.active(),
                dto.countryCode(),
                dto.cityCode(),
                dto.startDate(),
                dto.endDate(),
                dto.transportType(),
                dto.routeOrigin(),
                dto.routeDestination(),
                dto.address(),
                dto.latitude(),
                dto.longitude(),
                dto.images()
        );

        return svc.update(fixedDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        svc.delete(id);
    }
}
