package com.marketplace.service_review_questions.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.service_review_questions.model.Review;

public interface ReviewRepository extends JpaRepository <Review, Long> {

    Page<Review> findByServiceId(Long serviceId, Pageable pageable);
    
}
