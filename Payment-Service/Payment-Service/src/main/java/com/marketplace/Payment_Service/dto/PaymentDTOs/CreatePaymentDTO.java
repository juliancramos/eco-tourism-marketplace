package com.marketplace.Payment_Service.dto.PaymentDTOs;

import java.util.List;

import com.marketplace.Payment_Service.dto.PaymentDetailsDTOs.CreatePaymentDetailsDTO;
import com.marketplace.Payment_Service.dto.ReceiptDTOs.CreateReceiptDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreatePaymentDTO(
    @NotNull Long orderId,
    @NotNull Long userId,
    @NotNull @Positive Double amount,
    @NotBlank @Size(min = 3, max = 3) String currency,
    @NotBlank String paymentMethod,
    List<CreatePaymentDetailsDTO> details,
    CreateReceiptDTO receipt
) {}
