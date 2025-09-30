package com.marketplace.servicecatalog.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.servicecatalog.model.ProviderCacheEntity;
import com.marketplace.servicecatalog.repository.ProviderCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProviderCreatedConsumer {

    private final ProviderCacheRepository providerCacheRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "provider.created", groupId = "service-catalog")
    public void consume(String message) {
        try {
            ProviderCreatedEvent event = objectMapper.readValue(message, ProviderCreatedEvent.class);
            log.info("Received provider.created event: {}", event);

            ProviderCacheEntity entity = new ProviderCacheEntity(
                event.getProviderId(),
                event.getName(),
                event.getStatus()
            );

            providerCacheRepository.save(entity);
            log.info("Provider cached successfully: {}", entity.getId());

        } catch (Exception e) {
            log.error("Error processing provider.created event: {}", e.getMessage());
        }
    }
}
