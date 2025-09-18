package com.marketplace.userprofileservice.service;

import com.marketplace.userprofileservice.dto.CreateProviderRequestDTO;
import com.marketplace.userprofileservice.dto.CreateUserRequestDTO;
import com.marketplace.userprofileservice.dto.UserDTO;

public interface UserService {
    UserDTO createClient(CreateUserRequestDTO request);
    UserDTO createProvider(CreateProviderRequestDTO request);
}