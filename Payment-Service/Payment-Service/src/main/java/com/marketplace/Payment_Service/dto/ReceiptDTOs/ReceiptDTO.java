package com.marketplace.Payment_Service.dto.ReceiptDTOs;

import java.time.LocalDateTime;
import java.util.List;

public record ReceiptDTO(
    Long id,
    Long paymentId,
    String receiptNumber,
    LocalDateTime generatedAt,
    Double totals,
    List<String> items
) {}
