package com.marketplace.service_review_questions.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.service_review_questions.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findByServiceId(Long serviceId, Pageable pageable);

}
