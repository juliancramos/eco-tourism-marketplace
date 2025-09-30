package com.marketplace.Payment_Service.dto;

import java.time.LocalDateTime;

public record ReceiptDTO(
    Long id,
    String receiptNumber,
    LocalDateTime generatedAt,
    Double total
) {}
