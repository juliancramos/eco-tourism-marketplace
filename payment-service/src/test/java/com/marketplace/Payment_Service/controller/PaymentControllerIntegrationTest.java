package com.marketplace.Payment_Service.controller;

import com.marketplace.Payment_Service.model.Payment;
import com.marketplace.Payment_Service.model.PaymentMethod;
import com.marketplace.Payment_Service.model.PaymentStatus;
import com.marketplace.Payment_Service.repository.PaymentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PaymentControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private PaymentRepository paymentRepository;

    @BeforeEach
    void cleanDb() {
        paymentRepository.deleteAll();
    }

    private Payment createPayment(Long userId, PaymentMethod method, PaymentStatus status) {
        Payment payment = Payment.builder()
                .orderId(100L)
                .cartId(200L)
                .userId(userId)
                .total(50000.0)
                .status(status)
                .orderDate(LocalDateTime.now())
                .method(method)
                .transactionId("tx-test")
                .build();
        return paymentRepository.save(payment);
    }

    // ---------------------------------------------------------
    // (1) GET /payments/{id} -> devuelve el pago correcto
    // ---------------------------------------------------------
    @Test
    void getPayment_returnsPaymentDto() throws Exception {
        Payment saved = createPayment(1L, PaymentMethod.PSE, PaymentStatus.PENDING);

        mockMvc.perform(get("/payments/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.orderId").value(saved.getOrderId()))
                .andExpect(jsonPath("$.userId").value(saved.getUserId()))
                .andExpect(jsonPath("$.status").value(saved.getStatus().name()))
                .andExpect(jsonPath("$.paymentMethod").value(saved.getMethod().name()));
    }

    // ---------------------------------------------------------
    // (2) GET /payments/user/{userid} -> lista pagos por usuario
    // ---------------------------------------------------------
    @Test
    void listPayments_byUser_returnsOnlyUserPayments() throws Exception {
        createPayment(1L, PaymentMethod.PSE, PaymentStatus.PENDING);
        createPayment(1L, PaymentMethod.PSE, PaymentStatus.PENDING);
        createPayment(2L, PaymentMethod.PSE, PaymentStatus.PENDING);

        mockMvc.perform(get("/payments/user/{userid}", 1L)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].userId").value(1))
                .andExpect(jsonPath("$.content[1].userId").value(1));
    }

    // ---------------------------------------------------------
    // (3) POST /payments/{id}/pay/{cardNumber} - método PSE
    //     Debe COMPLETAR el pago y generar recibo
    // ---------------------------------------------------------
    @Test
    void simulatePayment_pseCompletesAndGeneratesReceipt() throws Exception {
        Payment saved = createPayment(1L, PaymentMethod.PSE, PaymentStatus.PENDING);

        mockMvc.perform(post("/payments/{id}/pay/{cardNumber}",
                        saved.getId(), "any-reference"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.receipt").exists());

        Payment reloaded = paymentRepository.findById(saved.getId()).orElseThrow();
        assertThat(reloaded.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(reloaded.getReceipt()).isNotNull();
    }

    // ---------------------------------------------------------
    // (4) POST /payments/{id}/pay/{cardNumber} - tarjeta inválida
    //     CREDIT_CARD con menos de 16 dígitos -> FAILED
    // ---------------------------------------------------------
    @Test
    void simulatePayment_invalidCardNumber_marksPaymentFailed() throws Exception {
        Payment saved = createPayment(1L, PaymentMethod.CREDIT_CARD, PaymentStatus.PENDING);

        mockMvc.perform(post("/payments/{id}/pay/{cardNumber}",
                        saved.getId(), "1234567890")) // < 16 dígitos
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FAILED"));

        Payment reloaded = paymentRepository.findById(saved.getId()).orElseThrow();
        assertThat(reloaded.getStatus()).isEqualTo(PaymentStatus.FAILED);
    }

    // ---------------------------------------------------------
    // (5) POST /payments/{id}/pay/{cardNumber} - tarjeta válida
    //     CREDIT_CARD con 16 dígitos -> COMPLETED + recibo
    // ---------------------------------------------------------
    @Test
    void simulatePayment_validCardNumber_completesAndGeneratesReceipt() throws Exception {
        Payment saved = createPayment(1L, PaymentMethod.CREDIT_CARD, PaymentStatus.PENDING);

        mockMvc.perform(post("/payments/{id}/pay/{cardNumber}",
                        saved.getId(), "1234567890123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.receipt").exists());

        Payment reloaded = paymentRepository.findById(saved.getId()).orElseThrow();
        assertThat(reloaded.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(reloaded.getReceipt()).isNotNull();
    }
}