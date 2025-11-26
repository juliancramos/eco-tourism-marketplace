package com.marketplace.userprofileservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.userprofileservice.model.Role;
import com.marketplace.userprofileservice.queue.ProviderCreatedEvent;
import com.marketplace.userprofileservice.queue.ProviderEventPublisher;
import com.marketplace.userprofileservice.queue.UserCreatedEvent;
import com.marketplace.userprofileservice.queue.UserEventPublisher;
import com.marketplace.userprofileservice.repository.RoleRepository;
import com.marketplace.userprofileservice.repository.UserRepository;
import com.marketplace.userprofileservice.service.KeycloakService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper mapper;

    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRepository userRepository;

    @MockBean private KeycloakService keycloakService;
    @MockBean private UserEventPublisher userEventPublisher;
    @MockBean private ProviderEventPublisher providerEventPublisher;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role client = new Role();
        client.setName("ROLE_CLIENT");
        client.setDescription("client role");
        roleRepository.save(client);

        Role provider = new Role();
        provider.setName("ROLE_PROVIDER");
        provider.setDescription("provider role");
        roleRepository.save(provider);

        Mockito.when(keycloakService.createKeycloakUser(any(), anyString()))
                .thenReturn("kc-test-user");
    }

    // 1. CREATE CLIENT
    @Test
    void createClient_createsUser_andPublishesUserEvent() throws Exception {

        String json = """
            {
              "email": "client@example.com",
              "password": "Secret123",
              "name": "Client Test"
            }
            """;

        mockMvc.perform(
                post("/users/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.email").value("client@example.com"))
        .andExpect(jsonPath("$.name").value("Client Test"));

        assertThat(userRepository.count()).isEqualTo(1);

        ArgumentCaptor<UserCreatedEvent> captor = ArgumentCaptor.forClass(UserCreatedEvent.class);

        Mockito.verify(userEventPublisher, times(1))
                .publishUserCreated(captor.capture());

        assertThat(captor.getValue().getName()).isEqualTo("Client Test");
        assertThat(captor.getValue().getStatus()).isEqualTo("ACTIVE");
    }

    // 2. CREATE PROVIDER
    @Test
    void createProvider_createsUser_andPublishesEvents() throws Exception {

        String json = """
            {
              "email": "provider@example.com",
              "password": "Secret123",
              "name": "Provider Owner",
              "tradeName": "EcoTours",
              "description": "Description test",
              "phoneNumber": "300000000",
              "webpage": "https://ecotours.test"
            }
            """;

        mockMvc.perform(
                post("/users/provider")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("Provider Owner"));

        // provider-created event
        Mockito.verify(providerEventPublisher, times(1))
                .publishProviderCreated(any(ProviderCreatedEvent.class));

        // user-created event
        Mockito.verify(userEventPublisher, times(1))
                .publishUserCreated(any(UserCreatedEvent.class));
    }

    // 3. GET USER BY ID
    @Test
    void getUserById_returnsCorrectUser() throws Exception {
        String json = """
            {
              "email": "id@example.com",
              "password": "Pass123",
              "name": "Lookup"
            }
            """;

        String response = mockMvc.perform(
                post("/users/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
        .andReturn().getResponse().getContentAsString();

        Long id = mapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Lookup"));
    }

    // 4. GET BY NAME
    @Test
    void getUserByName_returnsCorrectUser() throws Exception {

        String json = """
            {
              "email": "search@example.com",
              "password": "Pass123",
              "name": "SearchName"
            }
            """;

        mockMvc.perform(
                post("/users/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        mockMvc.perform(get("/users/name/{name}", "SearchName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("search@example.com"));
    }

    // 5. LIST ALL USERS
    @Test
    void listAllUsers_returnsList() throws Exception {

        mockMvc.perform(
                post("/users/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"email":"1@test.com","password":"aa","name":"U1"}
                """)
        );

        mockMvc.perform(
                post("/users/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"email":"2@test.com","password":"aa","name":"U2"}
                """)
        );

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // 6. UPDATE USER
    @Test
    void updateUser_updatesFields() throws Exception {

        String json = """
            {"email":"before@test.com","password":"aa","name":"Before"}
        """;

        String response = mockMvc.perform(
                post("/users/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andReturn().getResponse().getContentAsString();

        Long id = mapper.readTree(response).get("id").asLong();

        String updateJson = """
            {
              "name": "After",
              "imgUrl": "https://img.test/me.png"
            }
            """;

        mockMvc.perform(
                put("/users/update/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("After"))
        .andExpect(jsonPath("$.imgUrl").value("https://img.test/me.png"));
    }

    // 7. DELETE
    @Test
    void deleteUser_works() throws Exception {

        String json = """
            {"email":"delete@test.com","password":"aa","name":"DeleteMe"}
        """;

        String response = mockMvc.perform(
                post("/users/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andReturn().getResponse().getContentAsString();

        Long id = mapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/users/delete/{id}", id))
                .andExpect(status().isNoContent());

        assertThat(userRepository.existsById(id)).isFalse();
    }
}
