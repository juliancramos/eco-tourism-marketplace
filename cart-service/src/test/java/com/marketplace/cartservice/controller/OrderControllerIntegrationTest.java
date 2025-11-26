package com.marketplace.cartservice.controller;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.cartservice.dto.CheckoutRequestDTO;
import com.marketplace.cartservice.model.Cart;
import com.marketplace.cartservice.model.CartItem;
import com.marketplace.cartservice.model.Order;
import com.marketplace.cartservice.queue.MQCheckoutService;
import com.marketplace.cartservice.repository.CartItemRepository;
import com.marketplace.cartservice.repository.CartRepository;
import com.marketplace.cartservice.repository.OrderRepository;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OrderControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper mapper;

    @Autowired private CartRepository cartRepo;
    @Autowired private CartItemRepository itemRepo;
    @Autowired private OrderRepository orderRepo;

    // Bloquea el envío real a Kafka
    @MockBean private MQCheckoutService mqCheckout;

    @BeforeEach
    void cleanDb() {
        itemRepo.deleteAll();
        orderRepo.deleteAll();
        cartRepo.deleteAll();
    }

    // 1. CHECKOUT
    @Test
    void testCheckoutCreatesOrder() throws Exception {
        Cart c = new Cart();
        c.setUserId(10L);
        c.setCreationDate(LocalDateTime.now());
        c = cartRepo.save(c);

        CartItem item = new CartItem();
        item.setCart(c);
        item.setServiceId(999L);
        item.setQuantity(2);
        item.setDateAdded(LocalDateTime.now());
        itemRepo.save(item);

        CheckoutRequestDTO req = new CheckoutRequestDTO("CREDIT_CARD");

        // MOCK: evitar envío real a Kafka
        Mockito.doNothing().when(mqCheckout).paymentsRequest(Mockito.any());

        mockMvc.perform(
                post("/orders/{userId}/checkout", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req))
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.userId").value(10L))
        .andExpect(jsonPath("$.cartId").value(c.getId()));

        Assertions.assertEquals(1, orderRepo.count());
        Mockito.verify(mqCheckout, Mockito.times(1)).paymentsRequest(Mockito.any());
    }

    // 2. GET ORDER BY ID
    @Test
    void testGetOrder() throws Exception {
        Cart cart = new Cart();
        cart.setUserId(7L);
        cart.setCreationDate(LocalDateTime.now());
        cart = cartRepo.save(cart);

        Order o = new Order();
        o.setUserId(7L);
        o.setCartId(cart.getId());               
        o.setOrderDate(LocalDateTime.now());
        o.setStatus("CREATED");
        o.setTotal(0.0d);                        
        o.setPaymentMethod("CREDIT_CARD");
        o = orderRepo.save(o);

        mockMvc.perform(
                get("/orders/{id}", o.getId())
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("CREATED"))
        .andExpect(jsonPath("$.cartId").value(cart.getId()))
        .andExpect(jsonPath("$.userId").value(7L));
    }

    // 3. UPDATE ORDER STATUS
    @Test
    void testUpdateOrderStatus() throws Exception {
        Cart cart = new Cart();
        cart.setUserId(7L);
        cart.setCreationDate(LocalDateTime.now());
        cart = cartRepo.save(cart);

        Order o = new Order();
        o.setUserId(7L);
        o.setCartId(cart.getId());
        o.setOrderDate(LocalDateTime.now());
        o.setStatus("CREATED");
        o.setTotal(0.0d);
        o.setPaymentMethod("CREDIT_CARD");
        o = orderRepo.save(o);

        mockMvc.perform(
                put("/orders/{id}/status", o.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"PAID\"")
        )
        .andExpect(status().isOk())
        .andExpect(content().string("\"PAID\""));
    }

    // 4. GET ORDERS BY USER
    @Test
    void testGetOrdersByUser() throws Exception {
        Cart cart = new Cart();
        cart.setUserId(5L);
        cart.setCreationDate(LocalDateTime.now());
        cart = cartRepo.save(cart);

        Order o1 = new Order();
        o1.setUserId(5L);
        o1.setCartId(cart.getId());
        o1.setOrderDate(LocalDateTime.now());
        o1.setStatus("CREATED");
        o1.setTotal(10.0d);
        o1.setPaymentMethod("CREDIT_CARD");

        Order o2 = new Order();
        o2.setUserId(5L);
        o2.setCartId(cart.getId());
        o2.setOrderDate(LocalDateTime.now());
        o2.setStatus("PAID");
        o2.setTotal(20.0d);
        o2.setPaymentMethod("DEBIT_CARD");

        orderRepo.save(o1);
        orderRepo.save(o2);

        mockMvc.perform(
                get("/orders/user/{id}", 5L)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
    }
}