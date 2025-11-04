package com.marketplace.service_review_questions.dto.QuestionDTOs;

import java.time.LocalDateTime;
import java.util.List;

import com.marketplace.service_review_questions.dto.AnswerDTOs.AnswerDTO;

public record QuestionDTO(
    Long id,
    Long serviceId,
    Long userId,
    String text,
    LocalDateTime creationDate,
    List<AnswerDTO> answers
) {}
