package com.marketplace.userprofileservice.mapper;

import com.marketplace.userprofileservice.dto.CreateUserRequestDTO;
import com.marketplace.userprofileservice.dto.UserDTO;
import com.marketplace.userprofileservice.model.User;
import org.mapstruct.*;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(mapRolesToStrings(user))")
    UserDTO toDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "roles", ignore = true) // Assign at service
    //@Mapping (target = "password", ignore = true) //no password in DB, only in keycloak
    User fromCreateRequest(CreateUserRequestDTO request);

    
    default Set<String> mapRolesToStrings(User user) {
        if (user.getRoles() == null) return null;
        return user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());
    }
}
