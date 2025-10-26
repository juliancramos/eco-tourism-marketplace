package com.marketplace.servicecatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.servicecatalog.model.ProviderCacheEntity;

public interface ProviderCacheRepository extends JpaRepository<ProviderCacheEntity, Long> {
}
