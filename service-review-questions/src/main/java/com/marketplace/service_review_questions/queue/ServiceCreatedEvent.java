package com.marketplace.service_review_questions.queue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCreatedEvent {
    private Long serviceId;
    private String title;
    private String status;
}

