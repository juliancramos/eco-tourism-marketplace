package com.marketplace.servicecatalog.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateServiceDTO(
    @NotNull Long providerId,
    @NotNull Long categoryId,
    @NotBlank @Size(max = 160) String title,
    @Size(max = 4000) String description,
    @NotNull @Positive Double price,
    @NotBlank @Size(min = 3, max = 3) String currency,
    @NotNull Boolean active,
    @NotBlank @Size(min = 3, max = 3) String countryCode,
    @NotBlank @Size(min = 1, max = 5) String cityCode,
    @Size(max = 400) String address,
    List<ServiceImageDTO> images
) {}
