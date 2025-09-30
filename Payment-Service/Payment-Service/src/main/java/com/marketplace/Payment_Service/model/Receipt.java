package com.marketplace.Payment_Service.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Builder
@Entity
@Table(name = "RECEIPT")
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "RECEIPT_NUMBER", length = 100, nullable = false, unique = true)
    private String receiptNumber;

    @Column(name = "GENERATED_AT", nullable = false)
    private LocalDateTime generatedAt;

    @Column(name = "TOTAL", nullable = false)
    private Double total;

    @OneToOne
    @JoinColumn(name = "PAYMENT_ID", nullable = false)
    private Payment payment;
}
