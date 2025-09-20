package com.marketplace.servicecatalog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.marketplace.servicecatalog.dto.CreateServiceDTO;
import com.marketplace.servicecatalog.dto.ServiceCategoryDTO;
import com.marketplace.servicecatalog.dto.ServiceDTO;
import com.marketplace.servicecatalog.dto.UpdateServiceDTO;

public interface  ServiceCatalogService {
    ServiceCategoryDTO getCategory(Long id);
    ServiceDTO   getService(Long id);

    Page<ServiceCategoryDTO> listCategories(Pageable pageable);
    Page<ServiceDTO> listServices(Long categoryId, String cityCode, Boolean active, Pageable pageable);


    ServiceDTO create(CreateServiceDTO dto);
    ServiceDTO update(UpdateServiceDTO dto);
    void delete(Long id);
}
