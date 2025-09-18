package com.marketplace.userprofileservice.mapper;

import com.marketplace.userprofileservice.dto.ProviderDTO;
import com.marketplace.userprofileservice.model.Provider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProviderMapper {

    @Mapping(source = "user.id", target = "userId")
    ProviderDTO toDTO(Provider provider);
}
