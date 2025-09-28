package com.marketplace.Payment_Service.model;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PAYMENT")
@Builder
public class Payment { 
    @Id
    @Column(name = "ID", precision = 19, scale = 0, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ORDER_ID", precision = 19, scale = 0, nullable = false)
    private Long OrderId;

    @Column(name = "USER_ID", precision = 19, scale = 0, nullable = false)
    private Long userId;

    @Column(name = "AMOUNT", precision = 12, scale = 0, nullable = false)
    private Double amount;

    @Column(name = "CURRENCY", length = 3, nullable = false)
    private String currency;

    @Column(name = "STATUS", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "METHOD", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Column(name = "TRANSACTION_ID", length = 100, nullable = false)
    private String transactionId;

    @Column(name = "CALLBACKTOPIC", length = 100, nullable = false)
    private String callbackTopic;

    @Column(name = "CREATION_DATE", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "TIEMESTAMP", nullable = false)
    private LocalDateTime timestamp;

    @OneToMany(mappedBy = "PAYMENT_DETAIL", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentDetail> paymentDetails;

    @OneToOne(mappedBy = "RECEIPT", cascade = CascadeType.ALL, orphanRemoval = true)
    private Receipt receipt;
}
