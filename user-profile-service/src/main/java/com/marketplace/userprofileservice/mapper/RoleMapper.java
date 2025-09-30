package com.marketplace.userprofileservice.mapper;

import com.marketplace.userprofileservice.dto.RoleDTO;
import com.marketplace.userprofileservice.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDTO toDTO(Role role);

    @Mapping(target = "users", ignore = true)
    Role toEntity(RoleDTO dto);
}
