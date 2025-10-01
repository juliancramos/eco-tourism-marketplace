package com.marketplace.servicecatalog.queue;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceEventPublisher {

    @Value("${spring.cloud.stream.bindings.serviceCreated-out-0.destination}")
    private String topic;

    private final StreamBridge streamBridge;

    public void publishServiceCreated(ServiceCreatedEvent event) {
        Message<ServiceCreatedEvent> message = MessageBuilder.withPayload(event).build();

        boolean sent = streamBridge.send(topic, message);
        if (!sent) {
            throw new IllegalStateException("No se pudo publicar en el topic " + topic);
        }
    }
}