package com.marketplace.service_review_questions.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.service_review_questions.model.ServiceCacheEntity;

public interface ServiceCacheRepository extends JpaRepository<ServiceCacheEntity, Long> {
}