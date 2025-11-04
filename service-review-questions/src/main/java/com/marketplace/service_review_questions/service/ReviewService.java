 package com.marketplace.service_review_questions.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.marketplace.service_review_questions.dto.ReviewDTOs.CreateReviewDTO;
import com.marketplace.service_review_questions.dto.ReviewDTOs.ReviewDTO;
import com.marketplace.service_review_questions.dto.ReviewDTOs.UpdateReviewDTO;

public interface ReviewService {
    ReviewDTO getReview(Long id);
    Page<ReviewDTO> listReviews(Long serviceId, Pageable pageable);
    ReviewDTO createReview(CreateReviewDTO dto);
    ReviewDTO updateReview(UpdateReviewDTO dto);
    void deleteReview(Long id);
}