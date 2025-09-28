package com.marketplace.Payment_Service.service.impl;

import java.time.LocalDateTime;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.marketplace.Payment_Service.dto.OrderDTOs.OrderDTO;
import com.marketplace.Payment_Service.dto.PaymentDTOs.CreatePaymentDTO;
import com.marketplace.Payment_Service.dto.PaymentDTOs.PaymentDTO;
import com.marketplace.Payment_Service.dto.PaymentDTOs.UpdatePaymentDTO;
import com.marketplace.Payment_Service.mapper.PaymentMapper;
import com.marketplace.Payment_Service.model.Payment;
import com.marketplace.Payment_Service.model.PaymentStatus;
import com.marketplace.Payment_Service.repository.PaymentRepository;
import com.marketplace.Payment_Service.service.PaymentService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentDTO getPayment(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow();
        Hibernate.initialize(payment.getPaymentDetails());
        Hibernate.initialize(payment.getReceipt());
        return PaymentMapper.toDto(payment);
    }

    @Override
    public Page<PaymentDTO> listPayments(Long userid, Pageable pageable) {
        Page<Payment> page;

        if (userid != null) {
            page = paymentRepository.findByUserId(userid, pageable);
        } else {
            page = paymentRepository.findAll(pageable);
        }

        return page.map(PaymentMapper::toDto);
    }

    @Override
    public PaymentDTO createPayment(CreatePaymentDTO dto, OrderDTO order) {
        Payment payment = new Payment();
        PaymentMapper.applyCreate(payment, dto, order,LocalDateTime.now());
        payment = paymentRepository.save(payment);
        return PaymentMapper.toDto(payment);
    }

    @Override
    public PaymentDTO updatePayment(UpdatePaymentDTO dto) {
        Payment payment = paymentRepository.findById(dto.id()).orElseThrow();
        PaymentMapper.applyUpdate(payment, dto);
        payment = paymentRepository.save(payment);
        return PaymentMapper.toDto(payment);
    }

    @Override
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow();
        paymentRepository.delete(payment);
    }

    @Override
    public void processPayment(OrderDTO order){
        Payment payment = new Payment();
        payment.setOrderId(order.orderId());
        payment.setUserId(order.userId());
        payment.setAmount(order.total());
        payment.setCurrency(null);
        payment.setStatus(PaymentStatus.valueOf(order.status()));

    }
 
}
