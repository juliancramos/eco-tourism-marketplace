package com.marketplace.service_review_questions.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ANSWER")
@Builder
public class Answer {
    @Id
    @Column(name = "ID", precision = 19, scale = 0, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TEXT", length = 4000, nullable = false)
    private String text;

    @Column(name = "CREATION_DATE", nullable = false)
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "questionId")
    private Question question;
}
