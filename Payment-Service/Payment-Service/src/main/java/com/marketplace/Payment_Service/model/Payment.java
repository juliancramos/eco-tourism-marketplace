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
@Builder
@Entity
@Table(name = "PAYMENT")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // coincide con "id" de la orden recibida

    @Column(name = "ORDER_ID", nullable = false)
    private Long orderId;

    @Column(name = "CART_ID", nullable = false)
    private Long cartId;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "TOTAL", nullable = false)
    private Double total;

    @Column(name = "STATUS", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;  // PENDING, PAID, FAILED

    @Column(name = "ORDER_DATE", nullable = false)
    private LocalDateTime orderDate;

    // 🔹 Datos propios del pago
    @Column(name = "METHOD", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Column(name = "TRANSACTION_ID", length = 100)
    private String transactionId;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentDetail> items;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Receipt receipt;
}
