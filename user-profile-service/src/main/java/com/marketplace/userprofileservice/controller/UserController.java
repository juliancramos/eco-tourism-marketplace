package com.marketplace.userprofileservice.controller;

import com.marketplace.userprofileservice.dto.CreateUserRequestDTO;
import com.marketplace.userprofileservice.dto.CreateProviderRequestDTO;
import com.marketplace.userprofileservice.dto.UserDTO;
import com.marketplace.userprofileservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Create a new client user
    @PostMapping("/client")
    public ResponseEntity<UserDTO> createClient(@Valid @RequestBody CreateUserRequestDTO request) {
        UserDTO created = userService.createClient(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    // Create a new provider user
    @PostMapping("/provider")
    public ResponseEntity<UserDTO> createProvider(@Valid @RequestBody CreateProviderRequestDTO request) {
        UserDTO created = userService.createProvider(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }
}
