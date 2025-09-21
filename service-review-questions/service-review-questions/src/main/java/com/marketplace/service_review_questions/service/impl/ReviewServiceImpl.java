package com.marketplace.service_review_questions.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.marketplace.service_review_questions.dto.ReviewDTOs.CreateReviewDTO;
import com.marketplace.service_review_questions.dto.ReviewDTOs.ReviewDTO;
import com.marketplace.service_review_questions.dto.ReviewDTOs.UpdateReviewDTO;
import com.marketplace.service_review_questions.mapper.ReviewMapper;
import com.marketplace.service_review_questions.model.Review;
import com.marketplace.service_review_questions.repository.ReviewRepository;
import com.marketplace.service_review_questions.service.ReviewService;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    
    private final ReviewRepository reviewRepository;
    
    @Override
    public ReviewDTO getReview(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow();
        return new ReviewDTO(
            review.getId(),
            review.getServiceId(),
            review.getUserId(),
            review.getStars(),
            review.getCommentText(),
            review.getCreationDate().toString()
        );
    }

    @Override
    public Page<ReviewDTO> listReviews(Long serviceId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByServiceId(serviceId, pageable);
        return reviews.map(review -> new ReviewDTO(
            review.getId(),
            review.getServiceId(),
            review.getUserId(),
            review.getStars(),
            review.getCommentText(),
            review.getCreationDate().toString()
        ));
    }

    @Override
    public ReviewDTO createReview(CreateReviewDTO dto) {
        Review c = new Review();
        ReviewMapper.applyCreate(c, dto, java.time.LocalDateTime.now());
        reviewRepository.save(c);
        return ReviewMapper.toDto(c);
    }

    @Override
    public ReviewDTO updateReview(UpdateReviewDTO dto) {
        Review e = reviewRepository.findById(dto.serviceId()).orElseThrow();
        ReviewMapper.applyUpdate(e, dto);
        reviewRepository.save(e);
        return ReviewMapper.toDto(e);
    }
        

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
    
}
