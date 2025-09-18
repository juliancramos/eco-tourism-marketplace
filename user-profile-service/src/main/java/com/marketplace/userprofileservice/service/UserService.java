package com.marketplace.userprofileservice.service;

import java.util.List;

import com.marketplace.userprofileservice.dto.CreateProviderRequestDTO;
import com.marketplace.userprofileservice.dto.CreateUserRequestDTO;
import com.marketplace.userprofileservice.dto.UserDTO;

public interface UserService {
    UserDTO createClient(CreateUserRequestDTO request);
    UserDTO createProvider(CreateProviderRequestDTO request);
    UserDTO getById(Long id);
    UserDTO getByName(String name);
    List<UserDTO> getAll();
    UserDTO update(Long id, UserDTO dto);
    void delete(Long id);
}