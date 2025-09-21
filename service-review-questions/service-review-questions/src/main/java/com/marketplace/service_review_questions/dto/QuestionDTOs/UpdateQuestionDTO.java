package com.marketplace.service_review_questions.dto.QuestionDTOs;

import java.util.List;

import com.marketplace.service_review_questions.dto.AnswerDTOs.AnswerDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateQuestionDTO(
    @NotNull Long id,
    @NotNull Long serviceId,
    @NotNull Long userId,
    @NotNull @Size(max = 4000) String text,
    List<AnswerDTO> answers  
){}
