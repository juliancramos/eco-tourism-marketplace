package com.marketplace.service_review_questions.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.service_review_questions.model.UserCacheEntity;

public interface UserCacheRepository extends JpaRepository<UserCacheEntity, Long> {
}
