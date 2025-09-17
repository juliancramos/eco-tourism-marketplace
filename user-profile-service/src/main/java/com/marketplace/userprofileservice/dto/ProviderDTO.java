package com.marketplace.userprofileservice.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderDTO {
    private Long id;
    private Long userId;
    private String tradeName;
    private String description;
    private String phoneNumber;
    private String webpage;
    private LocalDateTime creationDate;
}
