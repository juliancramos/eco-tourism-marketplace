package com.marketplace.Payment_Service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.Payment_Service.dto.PaymentDTO;
import com.marketplace.Payment_Service.service.PaymentService;

import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable("id") Long id) {
        PaymentDTO paymentDTO = paymentService.getPayment(id);
        return new ResponseEntity<>(paymentDTO, HttpStatus.OK);
    }

    // Endpoint para listar pagos, opcionalmente por usuario
    @GetMapping
    public ResponseEntity<Page<PaymentDTO>> listPayments(
            @RequestParam(value = "userid", required = false) Long userid,
            Pageable pageable) {
        Page<PaymentDTO> payments = paymentService.listPayments(userid, pageable);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<PaymentDTO> simulatePayment(
            @PathVariable("id") Long paymentId,
            @RequestParam("cardNumber") String cardNumber) {
        PaymentDTO paymentDTO = paymentService.simulatePayment(paymentId, cardNumber);
        return new ResponseEntity<>(paymentDTO, HttpStatus.OK);
    }
    
}
