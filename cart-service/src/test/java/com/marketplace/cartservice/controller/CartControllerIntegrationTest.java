package com.marketplace.cartservice.controller;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.cartservice.dto.AddCartItemRequestDTO;
import com.marketplace.cartservice.model.Cart;
import com.marketplace.cartservice.model.CartItem;
import com.marketplace.cartservice.repository.CartItemRepository;
import com.marketplace.cartservice.repository.CartRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CartControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper mapper;

    @Autowired private CartRepository cartRepo;
    @Autowired private CartItemRepository itemRepo;

    @BeforeEach
    void cleanDb() {
        itemRepo.deleteAll();
        cartRepo.deleteAll();
    }

    // ---------------------------------------------
    // 1. CREATE CART
    // ---------------------------------------------
    @Test
    void testCreateCart() throws Exception {
        mockMvc.perform(
                post("/cart/user/{userId}", 99L)
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.userId").value(99L));

        Assertions.assertEquals(1, cartRepo.count());
    }

    // ---------------------------------------------
    // 2. GET CART BY ID
    // ---------------------------------------------
    @Test
    void testGetCart() throws Exception {
        Cart c = new Cart();
        c.setUserId(10L);
        c.setCreationDate(LocalDateTime.now());
        c = cartRepo.save(c);

        mockMvc.perform(
                get("/cart/{id}", c.getId())
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value(10L));
    }

    // ---------------------------------------------
    // 3. GET CART BY USER ID (CURRENT)
    // ---------------------------------------------
    @Test
    void testGetCartByUserId() throws Exception {
        Cart c = new Cart();
        c.setUserId(22L);
        c.setCreationDate(LocalDateTime.now());
        c = cartRepo.save(c);

        mockMvc.perform(
                get("/cart/user/{userId}/current", 22L)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value(22L));
    }

    // ---------------------------------------------
    // 4. ADD CART ITEM
    // ---------------------------------------------
    @Test
    void testAddCartItem() throws Exception {
        Cart c = new Cart();
        c.setUserId(5L);
        c.setCreationDate(LocalDateTime.now());
        c = cartRepo.save(c);

        AddCartItemRequestDTO req = new AddCartItemRequestDTO(
                777L, // serviceId
                2
        );

        mockMvc.perform(
                post("/cart/{cartId}/items", c.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req))
        )
        .andExpect(status().isCreated());

        Assertions.assertEquals(1, itemRepo.count());
    }

    // ---------------------------------------------
    // 5. REMOVE CART ITEM
    // ---------------------------------------------
    @Test
    void testRemoveCartItem() throws Exception {
        Cart c = new Cart();
        c.setUserId(5L);
        c.setCreationDate(LocalDateTime.now());
        c = cartRepo.save(c);

        CartItem item = new CartItem();
        item.setCart(c);
        item.setServiceId(555L);
        item.setQuantity(1);
        item.setDateAdded(LocalDateTime.now());
        item = itemRepo.save(item);

        mockMvc.perform(
                delete("/cart/{cartId}/items/{itemId}", c.getId(), item.getId())
        )
        .andExpect(status().isNoContent());

        Assertions.assertEquals(0, itemRepo.count());
    }

    // ---------------------------------------------
    // 6. CLEAR CART
    // ---------------------------------------------
    @Test
    void testClearCart() throws Exception {
        Cart c = new Cart();
        c.setUserId(9L);
        c.setCreationDate(LocalDateTime.now());
        c = cartRepo.save(c);

        CartItem i1 = new CartItem();
        i1.setCart(c);
        i1.setServiceId(1L);
        i1.setQuantity(1);
        i1.setDateAdded(LocalDateTime.now());
        itemRepo.save(i1);

        mockMvc.perform(
                post("/cart/{cartId}/clear", c.getId())
        )
        .andExpect(status().isNoContent());

        Assertions.assertEquals(0, itemRepo.count());
    }
}