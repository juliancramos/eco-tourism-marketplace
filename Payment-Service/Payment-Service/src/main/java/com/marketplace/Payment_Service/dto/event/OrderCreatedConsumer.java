package com.marketplace.Payment_Service.dto.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.Payment_Service.dto.OrderDTOs.OrderDTO;
import com.marketplace.Payment_Service.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCreatedConsumer {
    private final PaymentService paymentService;

    @KafkaListener(topics = "payments.request", groupId = "payment-service-group")
    public void consumer(OrderDTO orderDTO) {
        try {
            log.info("Received message for order ID: {}", orderDTO.id());
            paymentService.createPayment(orderDTO);
            log.info("Processed payment for order ID: {}", orderDTO.id());
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
        }
    }       
}
