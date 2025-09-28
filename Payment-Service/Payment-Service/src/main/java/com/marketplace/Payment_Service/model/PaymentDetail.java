package com.marketplace.Payment_Service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "PAYMENT_DETAIL")
@Builder
public class PaymentDetail {
    @Id
    @Column(name = "ID", precision = 19, scale = 0, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "SERVICE_ID", precision = 19, scale = 0, nullable = false)
    private Long serviceId;

    @Column(name = "SERVICE_NAME", length = 100, nullable = false)
    private String serviceName;

    @Column(name = "QUANTITY", precision = 10, scale = 0, nullable = false)
    private Integer quantity;

    @Column(name = "UNIT_PRICE", precision = 12, scale = 0, nullable = false)
    private Double unitPrice;

    @Column(name = "SUBTOTAL", precision = 12, scale = 0, nullable = false)
    private Double subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_ID", nullable = false)
    private Payment payment;
}
