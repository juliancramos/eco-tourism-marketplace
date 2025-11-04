package com.marketplace.service_review_questions.dto.AnswerDTOs;

import java.time.LocalDateTime;

public record AnswerDTO(
    String text,
    LocalDateTime creationDate
    ) {}
