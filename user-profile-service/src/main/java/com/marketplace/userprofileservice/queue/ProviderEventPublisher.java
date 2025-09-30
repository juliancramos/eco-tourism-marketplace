package com.marketplace.userprofileservice.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.messaging.Message;


@Service
public class ProviderEventPublisher {

    @Value("${spring.cloud.stream.bindings.providerCreated-out-0.destination}")
    private String topic;

    @Autowired
    private StreamBridge streamBridge;

    public void publishProviderCreated(ProviderCreatedEvent event) {
        Message<ProviderCreatedEvent> message = MessageBuilder.withPayload(event).build();

        boolean sent = streamBridge.send(topic, message);
        if (!sent) {
            throw new IllegalStateException("No se pudo publicar en el topic " + topic);
        }
    }
}
