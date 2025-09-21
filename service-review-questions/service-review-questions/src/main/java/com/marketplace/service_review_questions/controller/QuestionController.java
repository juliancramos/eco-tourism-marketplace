package com.marketplace.service_review_questions.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.service_review_questions.dto.AnswerDTOs.AnswerDTO;
import com.marketplace.service_review_questions.dto.AnswerDTOs.CreateAnswerDTO;
import com.marketplace.service_review_questions.dto.AnswerDTOs.UpdateAnswerDTO;
import com.marketplace.service_review_questions.dto.QuestionDTOs.CreateQuestionDTO;
import com.marketplace.service_review_questions.dto.QuestionDTOs.QuestionDTO;
import com.marketplace.service_review_questions.dto.QuestionDTOs.UpdateQuestionDTO;
import com.marketplace.service_review_questions.service.QuestionService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/questions")
@AllArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/{id}")
    public QuestionDTO get(@PathVariable Long id) {
        return questionService.getQuestion(id);
    }

    @GetMapping
    public Page<QuestionDTO> list(
            @RequestParam(required = false) Long serviceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return questionService.listQuestions(serviceId, PageRequest.of(page, size));
    }

    @PostMapping
    public QuestionDTO create(@RequestBody CreateQuestionDTO dto) {
        return questionService.create(dto);
    }
    
    @PutMapping("/{id}")
    public QuestionDTO update(@PathVariable Long id, @Valid @RequestBody UpdateQuestionDTO dto) {
        return questionService.update(new UpdateQuestionDTO(
                id, dto.serviceId(), dto.userId(), dto.text(), dto.answers() ));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        questionService.delete(id);
    }

    @PostMapping("/{id}/answers")
    public AnswerDTO reply(
            @PathVariable Long id,
            @Valid @RequestBody CreateAnswerDTO dto) {
        return questionService.reply(id, dto);
    }

    @PutMapping("/{questionId}/answers")
    public AnswerDTO updateAnswer(
            @PathVariable Long questionId,
            @Valid @RequestBody UpdateAnswerDTO dto) {
        return questionService.updateAnswer(questionId, dto);
    }
}
