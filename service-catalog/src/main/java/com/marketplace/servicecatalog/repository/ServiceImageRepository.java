package com.marketplace.servicecatalog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.servicecatalog.model.ServiceImage;

public interface ServiceImageRepository extends JpaRepository<ServiceImage, Long> {
  Optional<List<ServiceImage>> findByService_IdOrderByPositionAsc(Long serviceId);
}