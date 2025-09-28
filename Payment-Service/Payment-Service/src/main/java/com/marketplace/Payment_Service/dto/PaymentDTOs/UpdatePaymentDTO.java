package com.marketplace.Payment_Service.dto.PaymentDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdatePaymentDTO(
	@NotNull Long id,
	@NotBlank String status,
	@NotBlank  String callbackTopic
) {}
