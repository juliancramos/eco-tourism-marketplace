package com.marketplace.userprofileservice.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String email;
    private String name;
    private String imgUrl;
    private Boolean active;
    private LocalDateTime creationDate;

    private Set<String> roles; 
}
