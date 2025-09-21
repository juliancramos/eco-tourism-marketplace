package com.marketplace.service_review_questions.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Question {
    @Id
    @Column(name = "ID", precision = 19, scale = 0, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "SERVICE_ID", precision = 19, scale = 0, nullable = false)
    private Long serviceId;

    @Column(name = "USER_ID", precision = 19, scale = 0, nullable = false)
    private Long userId;

    @Column(name = "TEXT", length = 4000, nullable = false)
    private String text;

    @Column(name = "CREATION_DATE", nullable = false)
    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;
}
