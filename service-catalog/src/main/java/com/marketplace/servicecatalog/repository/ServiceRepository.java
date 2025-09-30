package com.marketplace.servicecatalog.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.marketplace.servicecatalog.model.ServiceEntity;


public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
  Page<ServiceEntity> findByCategoryIdAndActive(Long categoryId, Boolean active, Pageable pageable);

  Page<ServiceEntity> findByCityCodeAndActive(String cityCode, Boolean active, Pageable pageable);

  Page<ServiceEntity> findByActive(Boolean active, Pageable pageable);

  Page<ServiceEntity> findByProviderId(Long providerId, Pageable pageable);

  Page<ServiceEntity> findByCountryCode(String countryCode, Pageable pageable);

  Page<ServiceEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);

  Page<ServiceEntity> findByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);

}
