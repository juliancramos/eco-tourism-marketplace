package com.marketplace.Payment_Service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.marketplace.Payment_Service.dto.OrderDTOs.OrderDTO;
import com.marketplace.Payment_Service.dto.PaymentDTOs.CreatePaymentDTO;
import com.marketplace.Payment_Service.dto.PaymentDTOs.PaymentDTO;
import com.marketplace.Payment_Service.dto.PaymentDTOs.UpdatePaymentDTO;


public interface PaymentService {
    PaymentDTO getPayment(Long id);
    Page<PaymentDTO> listPayments(Long userId, Pageable pageable);
    PaymentDTO createPayment(CreatePaymentDTO dto, OrderDTO orderDTO);
    PaymentDTO updatePayment(UpdatePaymentDTO dto);
    void deletePayment(Long id);
    public void processPayment(OrderDTO orderDTO);
}
