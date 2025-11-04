package com.marketplace.service_review_questions.service.impl;

import java.time.LocalDateTime;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.marketplace.service_review_questions.dto.AnswerDTOs.AnswerDTO;
import com.marketplace.service_review_questions.dto.AnswerDTOs.CreateAnswerDTO;
import com.marketplace.service_review_questions.dto.AnswerDTOs.UpdateAnswerDTO;
import com.marketplace.service_review_questions.dto.QuestionDTOs.CreateQuestionDTO;
import com.marketplace.service_review_questions.dto.QuestionDTOs.QuestionDTO;
import com.marketplace.service_review_questions.dto.QuestionDTOs.UpdateQuestionDTO;
import com.marketplace.service_review_questions.mapper.AnswerMapper;
import com.marketplace.service_review_questions.mapper.QuestionMapper;
import com.marketplace.service_review_questions.model.Answer;
import com.marketplace.service_review_questions.model.Question;
import com.marketplace.service_review_questions.repository.QuestionRepository;
import com.marketplace.service_review_questions.repository.ServiceCacheRepository;
import com.marketplace.service_review_questions.repository.UserCacheRepository;
import com.marketplace.service_review_questions.service.QuestionService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final UserCacheRepository userCacheRepository;
    private final ServiceCacheRepository serviceCacheRepository;

    @Override
    @Transactional
    public QuestionDTO getQuestion(Long id) {
        Question question = questionRepository.findById(id).orElseThrow();
        Hibernate.initialize(question.getAnswers());
        return QuestionMapper.toDto(question); 
    }

    @Override
    @Transactional
    public Page<QuestionDTO> listQuestions(Long serviceId, Pageable pageable) {
        Page<Question> page;
        if (serviceId != null) {
            page = questionRepository.findByServiceId(serviceId, pageable);
        } else {
            page = questionRepository.findAll(pageable);
        }
        return page.map(QuestionMapper::toDto);
    }

    @Override
    public QuestionDTO create(CreateQuestionDTO dto) {
        System.out.println("Antes de verificar usuario en pregutna");
        if (!userCacheRepository.existsById(dto.userId())) {
            throw new EntityNotFoundException("User not found with id: " + dto.userId());
        }
        System.out.println("Antes de verificar servicio en pregutna");
        if (!serviceCacheRepository.existsById(dto.serviceId())) {
            throw new EntityNotFoundException("Service not found with id: " + dto.serviceId());
        }
        System.out.println("Despues de verificar en pregutna");
        Question question = new Question();
        QuestionMapper.applyCreate(question, dto, LocalDateTime.now());
        question = questionRepository.save(question);
        return QuestionMapper.toDto(question);
    }

    @Override
    public QuestionDTO update(UpdateQuestionDTO dto) {
        Question question = questionRepository.findById(dto.id()).orElseThrow();
        QuestionMapper.applyUpdate(question, dto);
        question = questionRepository.save(question);
        return QuestionMapper.toDto(question);
    }

    @Override
    public void delete(Long id) {
        questionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public AnswerDTO reply(Long questionId, CreateAnswerDTO dto) {
        Question question = questionRepository.findById(questionId)
            .orElseThrow(() -> new RuntimeException("Question not found"));

        Answer answer = new Answer();
        answer.setText(dto.text());
        answer.setCreationDate(LocalDateTime.now());
        answer.setQuestion(question);

        question.getAnswers().add(answer);
        questionRepository.save(question); 

        return new AnswerDTO(answer.getText(), answer.getCreationDate());
    }

    @Override
    @Transactional
    public AnswerDTO updateAnswer(Long questionId, UpdateAnswerDTO dto) {
        Question question = questionRepository.findById(questionId)
            .orElseThrow(() -> new RuntimeException("Question not found"));

        Answer answer = question.getAnswers().stream()
            .filter(a -> a.getId().equals(dto.id()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Answer not found"));

        AnswerMapper.applyUpdate(answer, dto);
        questionRepository.save(question);

        return AnswerMapper.toDto(answer);
    }

    
}
