package com.marketplace.userprofileservice.service.impl;

import com.marketplace.userprofileservice.dto.CreateProviderRequestDTO;
import com.marketplace.userprofileservice.dto.CreateUserRequestDTO;
import com.marketplace.userprofileservice.dto.UserDTO;
import com.marketplace.userprofileservice.mapper.UserMapper;
import com.marketplace.userprofileservice.model.Client;
import com.marketplace.userprofileservice.model.Provider;
import com.marketplace.userprofileservice.model.Role;
import com.marketplace.userprofileservice.model.User;
import com.marketplace.userprofileservice.repository.RoleRepository;
import com.marketplace.userprofileservice.repository.UserRepository;
import com.marketplace.userprofileservice.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDTO createClient(CreateUserRequestDTO request) {
        User user = createUserWithRole(request, "ROLE_CLIENT");

        Client client = Client.builder()
                .user(user)
                .build();

        user.setClient(client);
        userRepository.save(user);

        return userMapper.toDTO(user);
    }

    @Override
    @Transactional
    public UserDTO createProvider(CreateProviderRequestDTO request) {
        User user = createUserWithRole(request, "ROLE_PROVIDER");

        Provider provider = Provider.builder()
                .user(user)
                .tradeName(request.getTradeName()) 
                .description(request.getDescription())
                .phoneNumber(request.getPhoneNumber())
                .webpage(request.getWebpage())
                .creationDate(LocalDateTime.now())
                .build();

        user.setProvider(provider);
        userRepository.save(user);

        return userMapper.toDTO(user);
    }

    private User createUserWithRole(CreateUserRequestDTO request, String roleName) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }

        User user = userMapper.fromCreateRequest(request);
        
        Role role = roleRepository.findByName(roleName)
            .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));

        user.setActive(true);
        user.setCreationDate(LocalDateTime.now());
        //Add roles
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);


        return userRepository.save(user);
    }


}
