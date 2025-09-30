package com.marketplace.userprofileservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CreateProviderRequestDTO extends CreateUserRequestDTO {

    @NotBlank
    private String tradeName;

    private String description;

    private String phoneNumber;

    private String webpage;
}
