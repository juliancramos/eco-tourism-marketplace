package com.marketplace.servicecatalog.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.marketplace.servicecatalog.model.ServiceEntity;

import graphql.com.google.common.base.Optional;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
  Page<ServiceEntity> findByCategoryIdAndActive(Long categoryId, Boolean active, Pageable pageable);

  Page<ServiceEntity> findByCityCodeAndActive(String cityCode, Boolean active, Pageable pageable);

  Page<ServiceEntity> findByActive(Boolean active, Pageable pageable);

}
