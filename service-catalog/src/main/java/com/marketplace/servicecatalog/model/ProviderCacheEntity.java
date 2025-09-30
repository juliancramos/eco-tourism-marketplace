package com.marketplace.servicecatalog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PROVIDER_CACHE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderCacheEntity {

    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "STATUS")
    private String status; 

}
