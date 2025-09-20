package com.marketplace.servicecatalog.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
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
@Table(name = "SERVICE")
@Builder

public class ServiceEntity {

    @Id
    @Column(name = "ID", precision = 19, scale = 0, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PROVIDER_ID", precision = 19, scale = 0, nullable = false)
    private Long providerId;

    @Column(name = "SERVICE_CATEGORYID", precision = 19, scale = 0, nullable = false)
    private Long categoryId;

    @Column(name = "TITLE", length = 160, nullable = false)
    private String title;

    @Column(name = "DESCRIPTION", length = 4000)
    private String description;

    @Column(name = "PRICE", precision = 12, scale = 0, nullable = false)
    private Double price;

    @Column(name = "CURRENCY", length = 3, nullable = false)
    private String currency;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active;

    @Column(name = "COUNTRY_CODE", length = 3, nullable = false)
    private String countryCode;

    @Column(name = "CITY_CODE", length = 5, nullable = false)
    private String cityCode;

    @Column(name = "ADDRESS", length = 400)
    private String address;

    @Column(name = "CREATION_DATE", nullable = false)
    private Instant creationDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<ServiceImage> images = new ArrayList<>();
    
}
