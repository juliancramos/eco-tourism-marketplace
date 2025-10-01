package com.marketplace.service_review_questions.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USER_CACHE")
@Getter
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class UserCacheEntity {
    @Id
    private Long id;

    private String name;

    private String status;
}
