package com.marketplace.Payment_Service.dto.PaymentDTOs;

import java.time.LocalDateTime;
import java.util.List;

import com.marketplace.Payment_Service.dto.PaymentDetailsDTOs.PaymentDetailDTO;
import com.marketplace.Payment_Service.dto.ReceiptDTOs.ReceiptDTO;

public record PaymentDTO(
	Long id,
    Long orderID,
    Long userId,
    Double amount,
    String currency,
    String status,
    String paymentMethod,
    String transactionId,
    String callbackTopic,
    LocalDateTime creationDate,
    LocalDateTime timeStamp,
    List<PaymentDetailDTO> details,
    ReceiptDTO receipt
) {}
