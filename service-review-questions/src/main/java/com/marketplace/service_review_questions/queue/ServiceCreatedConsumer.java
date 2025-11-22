package com.marketplace.service_review_questions.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.service_review_questions.model.ServiceCacheEntity;
import com.marketplace.service_review_questions.repository.ServiceCacheRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceCreatedConsumer {

    private final ServiceCacheRepository serviceCacheRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "service.created", groupId = "service-review-questions")
    public void consume(String message) {
        try {
            ServiceCreatedEvent event = objectMapper.readValue(message, ServiceCreatedEvent.class);
            log.info("Received service.created event: {}", event);

            ServiceCacheEntity entity = new ServiceCacheEntity(
                event.getServiceId(),
                event.getTitle(),
                event.getStatus()
            );

            serviceCacheRepository.save(entity);
            log.info("Service cached successfully: {}", entity.getId());

        } catch (Exception e) {
            log.error("Error processing service.created event: {}", e.getMessage());
        }
    }
}
