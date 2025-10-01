package com.marketplace.userprofileservice.queue;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderCreatedEvent {
    private Long providerId;
    private String name;
    private String status; // "ACTIVE" by default
}