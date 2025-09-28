package com.marketplace.cartservice.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.marketplace.cartservice.dto.OrderDTO;

@Service
public class MQCheckoutService {
    @Value("${spring.cloud.stream.bindings.paymentsRequest-out-0.destination}")
    private String queueName;
    @Autowired
    private StreamBridge streamBridge;

    public String getQueueName() {
        return queueName;
    }

    public void paymentsRequest(OrderDTO dto) {
        Message<OrderDTO> msg = MessageBuilder.withPayload(dto).build();
        streamBridge.send(this.queueName, msg);

        boolean ok = streamBridge.send(queueName, msg);
        if (!ok) {
            throw new IllegalStateException("No se pudo publicar en binding " + queueName);
        }
    }

}
