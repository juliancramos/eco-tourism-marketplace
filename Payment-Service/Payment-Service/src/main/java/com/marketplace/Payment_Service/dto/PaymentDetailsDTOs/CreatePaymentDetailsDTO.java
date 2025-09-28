package com.marketplace.Payment_Service.dto.PaymentDetailsDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreatePaymentDetailsDTO(
    @NotNull Long serviceId,
    @NotBlank @Size(max = 200) String ServiceName,
    @NotNull @Positive Integer quantity,
    @NotNull @Positive Double unitPrice,
    @NotNull @Positive Double subtotal
) {
}
