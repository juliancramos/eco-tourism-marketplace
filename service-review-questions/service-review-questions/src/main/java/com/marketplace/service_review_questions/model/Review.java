package com.marketplace.service_review_questions.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "REVIEW")
@Builder
public class Review {
    @Id
    @Column(name = "ID", precision = 19, scale = 0, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "SERVICE_ID", precision = 19, scale = 0, nullable = false)   
    private Long serviceId;

    @Column(name = "USER_ID", precision = 19, scale = 0, nullable = false)
    private Long userId;

    @Column(name = "STARS", precision = 2, scale = 0, nullable = false) 
    private Integer stars;

    @Column(name = "COMMENT_TEXT", length = 4000)
    private String commentText;

    @Column(name = "CREATION_DATE", nullable = false)   
    private LocalDateTime creationDate;
}
