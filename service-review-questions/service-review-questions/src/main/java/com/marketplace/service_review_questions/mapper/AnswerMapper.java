package com.marketplace.service_review_questions.mapper;

import java.time.LocalDateTime;

import com.marketplace.service_review_questions.dto.AnswerDTOs.AnswerDTO;
import com.marketplace.service_review_questions.dto.AnswerDTOs.UpdateAnswerDTO;
import com.marketplace.service_review_questions.model.Answer;
import com.marketplace.service_review_questions.model.Question;

public class AnswerMapper {

    // Convierte Answer -> AnswerDTO
    public static AnswerDTO toDto(Answer a) {
        if (a == null) return null;
        return new AnswerDTO(
            a.getText(),
            a.getCreationDate()
        );
    }

    // Aplica los datos de creación a una nueva Answer
    public static void applyCreate(Answer a, String text, LocalDateTime now, Question q) {
        a.setText(text);
        a.setCreationDate(now);
        a.setQuestion(q);
    }

    // Aplica los datos de UpdateAnswerDTO a Answer
    public static void applyUpdate(Answer a, UpdateAnswerDTO dto) {
        a.setText(dto.text());
        // No tocamos creationDate ni question
    }

    // Convierte AnswerDTO -> Answer (si lo necesitas)
    public static Answer toEntity(AnswerDTO dto, Question q) {
        if (dto == null) return null;
        Answer a = new Answer();
        a.setText(dto.text());
        a.setCreationDate(dto.creationDate());
        a.setQuestion(q);
        return a;
    }
}
