package com.marketplace.service_review_questions.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.service_review_questions.dto.ReviewDTOs.CreateReviewDTO;
import com.marketplace.service_review_questions.dto.ReviewDTOs.ReviewDTO;
import com.marketplace.service_review_questions.dto.ReviewDTOs.UpdateReviewDTO;
import com.marketplace.service_review_questions.service.ReviewService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/reviews")
@AllArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/{id}")
    public ReviewDTO get(@PathVariable Long id) {
        return reviewService.getReview(id);
    }

    @GetMapping
    public Page<ReviewDTO> list(
            @RequestParam Long serviceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return reviewService.listReviews(serviceId, PageRequest.of(page, size));
    }

    @PostMapping
    public ReviewDTO create(@Valid @RequestBody CreateReviewDTO dto) {
        return reviewService.createReview(dto);
    }

    @PutMapping("/{id}")
    public ReviewDTO update(@PathVariable Long id, @Valid @RequestBody UpdateReviewDTO dto) {
        return reviewService.updateReview(new UpdateReviewDTO(
                id, dto.userId(), dto.stars(), dto.commentText()));
    }   

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
    
}
