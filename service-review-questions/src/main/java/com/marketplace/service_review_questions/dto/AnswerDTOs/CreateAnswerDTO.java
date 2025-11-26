package com.marketplace.service_review_questions.dto.AnswerDTOs;

import jakarta.validation.constraints.Size;

public record CreateAnswerDTO(
    @Size(max = 4000) String text
) {}
