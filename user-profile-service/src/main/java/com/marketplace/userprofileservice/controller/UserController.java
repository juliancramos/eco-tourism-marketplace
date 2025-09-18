package com.marketplace.userprofileservice.controller;

import com.marketplace.userprofileservice.dto.CreateUserRequestDTO;
import com.marketplace.userprofileservice.dto.CreateProviderRequestDTO;
import com.marketplace.userprofileservice.dto.UserDTO;
import com.marketplace.userprofileservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.net.URI;
import java.util.List;
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

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<UserDTO> getUserByName(@PathVariable String name) {
        return ResponseEntity.ok(userService.getByName(name));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> listAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
