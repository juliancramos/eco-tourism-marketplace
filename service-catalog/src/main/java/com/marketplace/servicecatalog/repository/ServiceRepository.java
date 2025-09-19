package com.marketplace.servicecatalog.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.servicecatalog.model.Service;

public interface ServiceRepository extends JpaRepository<Service, Long>
{
  Optional<Page<Service>> findByCategoryIdAndActive(Long categoryId, Integer active, Pageable pageable);
  Optional<Page<Service>> findByCityCodeAndActive(String cityCode, Integer active, Pageable pageable);
}
