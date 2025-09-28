package com.marketplace.Payment_Service.dto.ReceiptDTOs;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateReceiptDTO(
    @NotNull Long paymentId,
    @NotBlank @Size(max = 100) String receiptNumber,
    @NotNull @Positive Double totals,
    List<String> items
) {
    
}
