package com.marketplace.Payment_Service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.Payment_Service.dto.PaymentDTO;
import com.marketplace.Payment_Service.service.PaymentService;

import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/payments")
@AllArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

     @GetMapping("/{id}")
    public PaymentDTO getPayment(@PathVariable Long id) {
        return paymentService.getPayment(id);
    }

    // Endpoint para listar pagos, opcionalmente por usuario
    @GetMapping("/user/{userid}")
    public Page<PaymentDTO> listPayments(
            @PathVariable Long userid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return  paymentService.listPayments(userid, PageRequest.of(page, size));
    }


    @PostMapping("/{id}/pay/{cardNumber}")
    public PaymentDTO simulatePayment(
            @PathVariable("id") Long paymentId,
            @PathVariable String cardNumber) {
         return paymentService.simulatePayment(paymentId, cardNumber);
    }
    
}
