package com.marketplace.servicecatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.servicecatalog.model.ServiceCategory;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {}
