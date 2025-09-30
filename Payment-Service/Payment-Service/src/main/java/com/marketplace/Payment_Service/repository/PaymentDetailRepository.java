package com.marketplace.Payment_Service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.Payment_Service.model.PaymentDetail;

public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {
    
}
