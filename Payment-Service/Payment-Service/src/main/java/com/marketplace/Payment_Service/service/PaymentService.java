package com.marketplace.Payment_Service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.marketplace.Payment_Service.dto.PaymentDTO;
import com.marketplace.Payment_Service.dto.OrderDTOs.OrderDTO;


public interface PaymentService {
    PaymentDTO getPayment(Long id);
    Page<PaymentDTO> listPayments(Long userId, Pageable pageable);
    PaymentDTO createPayment(OrderDTO orderDTO);
    PaymentDTO simulatePayment(Long id, String cardNumber);
    void deletePayment(Long id);
}
