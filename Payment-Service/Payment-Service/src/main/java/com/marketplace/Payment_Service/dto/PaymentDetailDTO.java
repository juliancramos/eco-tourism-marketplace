package com.marketplace.Payment_Service.dto;

public record PaymentDetailDTO(
    Long id,
    Long serviceId,
    Integer quantity,
    Double unitPrice
) {
}
