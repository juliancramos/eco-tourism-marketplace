package com.marketplace.Payment_Service.dto;

public record MetaDataDTO(
    String correlationId,
    String causationId,
    String producerService,
    String producerVersion
) {
    
}
