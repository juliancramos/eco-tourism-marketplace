package com.marketplace.service_review_questions.dto.ReviewDTOs;

public record ReviewDTO(
    Long id,
    Long serviceId,
    Long userId,
    Integer stars,
    String commentText,
    String creationDate) {} 
