package com.marketplace.service_review_questions.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.marketplace.service_review_questions.dto.AnswerDTOs.AnswerDTO;
import com.marketplace.service_review_questions.dto.QuestionDTOs.CreateQuestionDTO;
import com.marketplace.service_review_questions.dto.QuestionDTOs.QuestionDTO;
import com.marketplace.service_review_questions.dto.QuestionDTOs.UpdateQuestionDTO;
import com.marketplace.service_review_questions.model.Question;

public class QuestionMapper {
    // Convierte Question -> QuestionDTO
    public static QuestionDTO toDto(Question q) {
        List<AnswerDTO> answers;
        answers = q.getAnswers() == null ? List.of() :
                  q.getAnswers().stream()
                   .map(a -> new AnswerDTO(a.getText(), a.getCreationDate()))
                   .toList();

        return new QuestionDTO(
            q.getId(),
            q.getServiceId(),
            q.getUserId(),
            q.getText(),
            q.getCreationDate(),
            answers
        );
    }

    // Aplica los datos de CreateQuestionDTO a Question
    public static void applyCreate(Question q, CreateQuestionDTO dto, LocalDateTime now) {
        q.setServiceId(dto.serviceId());
        q.setUserId(dto.userId());
        q.setText(dto.text());
        q.setCreationDate(now);
        q.setAnswers(new ArrayList<>());
    }

    // Aplica los datos de UpdateQuestionDTO a Question
    public static void applyUpdate(Question q, UpdateQuestionDTO dto) {
        q.setServiceId(dto.serviceId());
        q.setUserId(dto.userId());
        q.setText(dto.text());
        // No actualizamos creationDate
        // Opcional: si quieres actualizar respuestas, se puede añadir aquí
    }

    // Convierte QuestionDTO -> Question (si lo necesitas)
    public static Question toEntity(QuestionDTO dto) {
        if (dto == null) return null;

        Question q = new Question();
        q.setId(dto.id());
        q.setServiceId(dto.serviceId());
        q.setUserId(dto.userId());
        q.setText(dto.text());
        q.setCreationDate(dto.creationDate());
        q.setAnswers(new ArrayList<>()); // Puedes mapear las respuestas si es necesario
        return q;
    }
}
