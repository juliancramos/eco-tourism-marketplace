package com.marketplace.Payment_Service.service.impl;

import java.time.LocalDateTime;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.marketplace.Payment_Service.dto.PaymentDTO;
import com.marketplace.Payment_Service.dto.OrderDTOs.OrderDTO;
import com.marketplace.Payment_Service.mapper.PaymentMapper;
import com.marketplace.Payment_Service.model.Payment;
import com.marketplace.Payment_Service.model.PaymentStatus;
import com.marketplace.Payment_Service.model.Receipt;
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
        Hibernate.initialize(payment.getItems());
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
    public PaymentDTO createPayment(OrderDTO order) {
        Payment payment = new Payment();
        PaymentMapper.applyCreate(payment, order,LocalDateTime.now());
        payment = paymentRepository.save(payment);
        return PaymentMapper.toDto(payment);
    }

    @Override
    public PaymentDTO simulatePayment(Long id, String cardNumber) {
        Payment payment = paymentRepository.findById(id).orElseThrow();
        String method = payment.getMethod().name();
        if(method.equals("PSE")){
            payment.setStatus(PaymentStatus.COMPLETED);
            createReceipt(payment);
        }
        if(method.equals("CREDIT_CARD") || method.equals("DEBIT_CARD")){
            if(cardNumber.length() != 16 || cardNumber == null){
                payment.setStatus(PaymentStatus.FAILED);
            }
            else{
                payment.setStatus(PaymentStatus.COMPLETED);
                createReceipt(payment);
            }   
        }
        return PaymentMapper.toDto(paymentRepository.save(payment));
    }


    public static void createReceipt(Payment e) {
        if (e == null) return;

        Receipt receipt = new Receipt();
        receipt.setPayment(e); // asociamos el pago
        receipt.setReceiptNumber(generateReceiptNumber()); // generamos número único
        receipt.setGeneratedAt(LocalDateTime.now());
        receipt.setTotal(e.getTotal() != null ? e.getTotal() : 0.0);

        e.setReceipt(receipt);
    }

    private static String generateReceiptNumber() {
        return "RCPT-" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + "-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow();
        paymentRepository.delete(payment);
    }
}
