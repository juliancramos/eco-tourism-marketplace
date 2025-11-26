package com.marketplace.service_review_questions.dto.ReviewDTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateReviewDTO(
    @NotNull Long serviceId,
    @NotNull Long userId,
    @NotNull @Positive Integer stars,
    @Size(max = 4000) String commentText) {
    
}
