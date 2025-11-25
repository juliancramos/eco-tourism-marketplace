package com.marketplace.cartservice.controller;

import com.marketplace.cartservice.dto.OrderDTO;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.Message;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.marketplace.cartservice.queue.MQCheckoutService;

@SpringBootTest
@ActiveProfiles("test")
class MQCheckoutServiceTest {

    @Autowired
    private MQCheckoutService mqCheckoutService;

    @MockBean
    private org.springframework.cloud.stream.function.StreamBridge streamBridge;

    @Test
    void paymentsRequest_sendsMessageToConfiguredBinding() {
        // given
        OrderDTO dto = new OrderDTO(
                1L,   
                2L,   
                10L,  
                100.0,
                "PENDING",
                "CREDIT_CARD",
                null,
                java.util.List.of()
        );

        when(streamBridge.send(anyString(), any(Message.class))).thenReturn(true);

        // when
        mqCheckoutService.paymentsRequest(dto);

        // then
        ArgumentCaptor<String> bindingCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Message<?>> msgCaptor = ArgumentCaptor.forClass(Message.class);

        verify(streamBridge, times(1)).send(bindingCaptor.capture(), msgCaptor.capture());

        assertThat(bindingCaptor.getValue()).isEqualTo("payments.request");
        assertThat(msgCaptor.getValue().getPayload()).isEqualTo(dto);
    }

    @Test
    void paymentsRequest_throwsIfSendReturnsFalse() {
        // given
        OrderDTO dto = new OrderDTO(
                1L, 2L, 10L, 100.0,
                "PENDING", "CREDIT_CARD", null, java.util.List.of()
        );

        when(streamBridge.send(anyString(), any(Message.class))).thenReturn(false);

        // when & then
        assertThrows(IllegalStateException.class, () -> mqCheckoutService.paymentsRequest(dto));
    }
}