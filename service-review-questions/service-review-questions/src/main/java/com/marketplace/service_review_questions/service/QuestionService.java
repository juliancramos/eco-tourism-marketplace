package com.marketplace.service_review_questions.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.marketplace.service_review_questions.dto.AnswerDTOs.AnswerDTO;
import com.marketplace.service_review_questions.dto.AnswerDTOs.CreateAnswerDTO;
import com.marketplace.service_review_questions.dto.AnswerDTOs.UpdateAnswerDTO;
import com.marketplace.service_review_questions.dto.QuestionDTOs.CreateQuestionDTO;
import com.marketplace.service_review_questions.dto.QuestionDTOs.QuestionDTO;
import com.marketplace.service_review_questions.dto.QuestionDTOs.UpdateQuestionDTO;

public interface QuestionService {
    QuestionDTO getQuestion(Long id);
    
    Page<QuestionDTO> listQuestions(Long serviceId, Pageable pageable);
    
    QuestionDTO create(CreateQuestionDTO dto);
    QuestionDTO update(UpdateQuestionDTO dto);
    void delete(Long id);

    AnswerDTO reply(Long questionId, CreateAnswerDTO dto);
    AnswerDTO updateAnswer(Long questionId, UpdateAnswerDTO dto);
}
