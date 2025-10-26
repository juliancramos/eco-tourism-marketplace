package com.marketplace.userprofileservice.service.impl;

import com.marketplace.userprofileservice.dto.CreateProviderRequestDTO;
import com.marketplace.userprofileservice.dto.CreateUserRequestDTO;
import com.marketplace.userprofileservice.dto.UserDTO;
import com.marketplace.userprofileservice.mapper.UserMapper;
import com.marketplace.userprofileservice.model.Client;
import com.marketplace.userprofileservice.model.Provider;
import com.marketplace.userprofileservice.model.Role;
import com.marketplace.userprofileservice.model.User;
import com.marketplace.userprofileservice.queue.ProviderCreatedEvent;
import com.marketplace.userprofileservice.queue.ProviderEventPublisher;
import com.marketplace.userprofileservice.queue.UserCreatedEvent;
import com.marketplace.userprofileservice.queue.UserEventPublisher;
import com.marketplace.userprofileservice.repository.RoleRepository;
import com.marketplace.userprofileservice.repository.UserRepository;
import com.marketplace.userprofileservice.service.KeycloakService;
import com.marketplace.userprofileservice.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final KeycloakService keycloakService;
    private final ProviderEventPublisher providerEventPublisher;
    private final UserEventPublisher userEventPublisher;


    @Override
    @Transactional
    public UserDTO createClient(CreateUserRequestDTO request) {
        User user = createUserWithRole(request, "ROLE_CLIENT");

        Client client = Client.builder()
                .user(user)
                .build();

        user.setClient(client);
        userRepository.save(user);

        //publish new user
        userEventPublisher.publishUserCreated(
            new UserCreatedEvent(user.getId(), user.getName(),"ACTIVE")
        );

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

        // publish event every time a provider is created
        providerEventPublisher.publishProviderCreated(
            new ProviderCreatedEvent(user.getProvider().getId(), provider.getTradeName(), "ACTIVE")
        );
        //publish new user
        userEventPublisher.publishUserCreated(
            new UserCreatedEvent(user.getId(), user.getName(),"ACTIVE")
        );

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

        try {
            String keycloakUserId = keycloakService.createKeycloakUser(request, roleName);
            user.setKeycloakUserId(keycloakUserId); 
        } catch (Exception e) {
            //e.printStackTrace(); complete message
            throw new RuntimeException("Failed to create user in Keycloak: " + e.getMessage(), e);
        }

        return userRepository.save(user); 
    
    }


    @Override
    @Transactional
    public UserDTO getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return userMapper.toDTO(user);
    }

    @Override
    @Transactional
    public UserDTO getByName(String name) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("User not found with name: " + name));
        return userMapper.toDTO(user);
    }

    @Override
    @Transactional
    public List<UserDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        // Update only if fields are not null
        if (dto.getName() != null && !dto.getName().isBlank()) {
            user.setName(dto.getName());
        }

        if (dto.getImgUrl() != null && !dto.getImgUrl().isBlank()) {
            user.setImgUrl(dto.getImgUrl());
        }

        // email and password don't change here (dedicated endpoints for that)

        User updated = userRepository.save(user);
        return userMapper.toDTO(updated);
    }


    @Override
    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }



}
