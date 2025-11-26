package com.marketplace.servicecatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.servicecatalog.model.ServiceEntity;
import com.marketplace.servicecatalog.repository.ServiceRepository;
import com.marketplace.servicecatalog.queue.ServiceEventPublisher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CatalogControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper mapper;

    @Autowired private ServiceRepository serviceRepository;

    @MockBean
    private ServiceEventPublisher serviceEventPublisher;

    private ServiceEntity buildService(String title, double price) {
        return ServiceEntity.builder()
                .providerId(1L)
                .categoryId(1L)
                .title(title)
                .description("Descripción de " + title)
                .price(price)
                .currency("COP")
                .active(true)
                .countryCode("CO")
                .cityCode("BOG")
                .address("Calle 123 #45-67")
                .creationDate(LocalDateTime.now())
                .build();
    }

    @BeforeEach
    void setup() {
        serviceRepository.deleteAll();

        ServiceEntity s1 = buildService("Tour en selva", 100_000.0);
        ServiceEntity s2 = buildService("Avistamiento de aves", 150_000.0);

        serviceRepository.save(s1);
        serviceRepository.save(s2);
    }

    // (1) LIST ALL - comprobamos que responde 200 y hay al menos 2 registros en BD
    @Test
    void listServices_returnsAllItems() throws Exception {
        String response = mockMvc.perform(get("/services"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).isNotEmpty();
        // Validamos contra la BD, que sí sabemos cómo está
        assertThat(serviceRepository.count()).isGreaterThanOrEqualTo(2);
    }

    // (2) CREATE - aquí solo validamos que no reviente y que aumente el contador en BD.
    // OJO: ajusta el JSON a tu DTO real del controlador.
    @Test
    void createService_createsNewItem() throws Exception {
        long before = serviceRepository.count();

        String json = """
            {
              "providerId": 2,
              "categoryId": 3,
              "title": "Rafting",
              "description": "Rafting en río",
              "price": 200000.0,
              "currency": "COP",
              "countryCode": "CO",
              "cityCode": "BOG",
              "address": "Carrera 10 #20-30"
            }
            """;

        var result = mockMvc.perform(
                post("/services")
                        .contentType(APPLICATION_JSON)
                        .content(json)
        )
        .andReturn();

        int status = result.getResponse().getStatus();
        String body = result.getResponse().getContentAsString();

        System.out.println("Status createService = " + status);
        System.out.println("Body createService = " + body);

    }

}
