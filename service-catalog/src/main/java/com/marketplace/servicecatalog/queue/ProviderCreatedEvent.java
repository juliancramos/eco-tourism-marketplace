package com.marketplace.servicecatalog.queue;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderCreatedEvent {
    private Long providerId;
    private String name;
    private String status;
}