package com.marketplace.Payment_Service.dto.PaymentDetailsDTOs;

public record PaymentDetailDTO(
    Long id,
    Long serviceId,
    String serviceName,
    Integer quantity,
    Double unitPrice,
    Double subtotal
) {
}
