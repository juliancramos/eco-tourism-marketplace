package com.marketplace.userprofileservice.mapper;

import com.marketplace.userprofileservice.dto.ClientDTO;
import com.marketplace.userprofileservice.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(source = "user.id", target = "userId")
    ClientDTO toDTO(Client client);
}
