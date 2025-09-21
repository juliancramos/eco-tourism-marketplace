package com.marketplace.service_review_questions.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.service_review_questions.model.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    
}
