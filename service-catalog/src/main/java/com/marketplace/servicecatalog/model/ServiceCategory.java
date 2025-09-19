package com.marketplace.servicecatalog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "SERVICE_CATEGORY")
@Builder
public class ServiceCategory {

    @Id
    @Column(name = "ID", nullable = false, precision = 19, scale = 0)
    private Long id;

    @Column(name = "NAME", length = 120, nullable = false)
    private String name;
}
