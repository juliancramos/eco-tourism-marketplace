package com.marketplace.Payment_Service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.Payment_Service.model.Receipt;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    
}
