package com.marketplace.service_review_questions.dto.AnswerDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateAnswerDTO(
    @NotNull Long id,    // id de la respuesta a actualizar
    @NotBlank String text
) {}
