package com.marketplace.Payment_Service.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
@Entity
@Table(name = "RECEIPT")
@Builder
public class Receipt {
    @Id
    @Column(name = "ID", precision = 19, scale = 0, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PAYMENT_ID", precision = 19, scale = 0, nullable = false)
    private Long paymentId;

    @Column(name = "RECEIPT_NUMBER", length = 100, nullable = false)
    private String receiptNumber;

    @Column(name = "GENERATED_AT", nullable = false)
    private LocalDateTime generatedAt;

    @Column(name = "TOTALS", precision = 12, scale = 0, nullable = false)
    private Double totals;

    @ElementCollection
    @CollectionTable(name = "RECEIPT_ITEMS", joinColumns = @JoinColumn(name = "RECEIPT_ID"))
    private List<String> items;

    @OneToOne
    @JoinColumn(name = "PAYMENT_ID", insertable = false, updatable = false)
    private Payment payment;
}
