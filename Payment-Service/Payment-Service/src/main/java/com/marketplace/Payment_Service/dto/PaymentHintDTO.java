package com.marketplace.Payment_Service.dto;

public record PaymentHintDTO(
    String paymentId,       // opcional
    String method,          // SIMULATED, CARD, etc.
    Double amount,
    String currency,
    String callbackTopic
) {
	
}
