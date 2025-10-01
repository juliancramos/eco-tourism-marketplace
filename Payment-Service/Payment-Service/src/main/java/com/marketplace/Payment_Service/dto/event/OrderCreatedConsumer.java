package com.marketplace.Payment_Service.dto.event;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.marketplace.Payment_Service.dto.OrderDTOs.OrderDTO;
import com.marketplace.Payment_Service.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCreatedConsumer {
    private final PaymentService paymentService;

    @Bean
    public java.util.function.Consumer<OrderDTO> paymentsRequest() {
        return orderDTO -> {
            try {
                log.info("Received message for order ID: {}", orderDTO.id());
                paymentService.createPayment(orderDTO);
                log.info("Processed payment for order ID: {}", orderDTO.id());
            } catch (Exception e) {
                log.error("Error processing message: {}", e.getMessage(), e);
            }
        };
    }      
}
