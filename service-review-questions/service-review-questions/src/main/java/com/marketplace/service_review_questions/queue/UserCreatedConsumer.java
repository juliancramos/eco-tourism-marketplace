package com.marketplace.service_review_questions.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.service_review_questions.model.UserCacheEntity;
import com.marketplace.service_review_questions.repository.UserCacheRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCreatedConsumer {

    private final UserCacheRepository userCacheRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "user.created", groupId = "service-review-questions")
    public void consume(String message) {
        try {
            UserCreatedEvent event = objectMapper.readValue(message, UserCreatedEvent.class);
            log.info("Received user.created event: {}", event);

            UserCacheEntity entity = new UserCacheEntity(
                event.getUserId(),
                event.getName(),
                event.getStatus()
            );

            userCacheRepository.save(entity);
            log.info("User cached successfully: {}", entity.getId());

        } catch (Exception e) {
            log.error("Error processing user.created event: {}", e.getMessage());
        }
    }
}
