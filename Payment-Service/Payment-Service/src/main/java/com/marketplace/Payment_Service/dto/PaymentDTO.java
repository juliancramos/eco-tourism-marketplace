package com.marketplace.Payment_Service.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PaymentDTO(
    Long id,                     // id propio del pago
    Long orderId,                // referencia a la orden
    Long cartId,                 // referencia al carrito
    Long userId,
    Double total,                // antes "amount"
    String status,               // PENDING, PAID, FAILED
    String paymentMethod,        // CREDIT_CARD, DEBIT_CARD, PSE, etc.
    String transactionId,        // generado por el servicio de pagos
    LocalDateTime orderDate,     // fecha de la orden (del mensaje Kafka)
    List<PaymentDetailDTO> items, // detalle de servicios
    ReceiptDTO receipt           // recibo generado en este microservicio
) {}
