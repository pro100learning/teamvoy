package com.teamvoy.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.teamvoy.shop.dto.OrderDTO;
import com.teamvoy.shop.dto.OrderUpdateDTO;
import com.teamvoy.shop.dto.UserDTO;
import com.teamvoy.shop.entity.enums.Status;
import com.teamvoy.shop.util.TokenGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WebAppConfiguration
@ContextConfiguration
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application-test.properties")
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-phone-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-basket-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-order-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-order-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = {"/create-basket-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = {"/create-phone-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithMockUser(username = "test2@gmail.com", password = "$2a$10$aNwQFNT/GxmWNYaG9CZ4BuoMh65OdysRNie5gJqHxyzHwkrx7gB/6", roles = {"USER", "MODERATOR"})
class OrderControllerTest {

    private final String TOKEN = TokenGeneratorService.createToken("test2@gmail.com");

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private UserDTO user;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        //objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        user = new UserDTO().toBuilder()
                .id(2L)
                .surname("Татарчук")
                .name("Андрій")
                .email("test2@gmail.com")
                .phone("380972553992")
                .build();
    }

    @Test
    void getAllOrders() throws Exception {
        mockMvc.perform(get("/orders")
                        .header("Authorization", "Bearer " + TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    void getOrder() throws Exception {
        OrderDTO expected = new OrderDTO().toBuilder()
                .id(5L)
                .user(user)
                .status(Status.CREATED)
                .price(2700)
                .paid(false)
                .createdAt(LocalDateTime.of(2022, 6, 30, 1, 10, 0, 0))
                .build();

        mockMvc.perform(get("/orders/{id}", 5)
                        .header("Authorization", "Bearer " + TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.user").value(expected.getUser()))
                .andExpect(jsonPath("$.status").value(expected.getStatus().name()))
                .andExpect(jsonPath("$.price").value(expected.getPrice()))
                .andExpect(jsonPath("$.paid").value(expected.isPaid()));
    }

    @Test
    void create() throws Exception {
        OrderDTO expected = new OrderDTO().toBuilder()
                .id(6L)
                .user(user)
                .status(Status.CREATED)
                .price(2700)
                .paid(false)
                .createdAt(LocalDateTime.now())
                .build();

        mockMvc.perform(post("/orders")
                        .header("Authorization", "Bearer " + TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.user").value(expected.getUser()))
                .andExpect(jsonPath("$.status").value(expected.getStatus().name()))
                .andExpect(jsonPath("$.price").value(expected.getPrice()))
                .andExpect(jsonPath("$.paid").value(expected.isPaid()));
    }

    @Test
    void pay() throws Exception {
        OrderDTO expected = new OrderDTO().toBuilder()
                .id(5L)
                .user(user)
                .status(Status.PAID)
                .price(2700)
                .paid(true)
                .createdAt(LocalDateTime.of(2022, 6, 30, 1, 10, 0, 0))
                .build();

        mockMvc.perform(post("/orders/{id}", 5)
                        .header("Authorization", "Bearer " + TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.user").value(expected.getUser()))
                .andExpect(jsonPath("$.status").value(expected.getStatus().name()))
                .andExpect(jsonPath("$.price").value(expected.getPrice()))
                .andExpect(jsonPath("$.paid").value(expected.isPaid()));
    }

    @Test
    void update() throws Exception {
        OrderUpdateDTO dto = new OrderUpdateDTO().toBuilder()
                .id(5L)
                .status(Status.CANCELED)
                .build();

        OrderDTO expected = new OrderDTO().toBuilder()
                .id(5L)
                .user(user)
                .status(Status.CANCELED)
                .price(2700)
                .paid(false)
                .createdAt(LocalDateTime.of(2022, 6, 30, 1, 10, 0, 0))
                .build();

        mockMvc.perform(put("/orders")
                        .header("Authorization", "Bearer " + TOKEN)
                        .content(objectMapper.writeValueAsBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.user").value(expected.getUser()))
                .andExpect(jsonPath("$.status").value(expected.getStatus().name()))
                .andExpect(jsonPath("$.price").value(expected.getPrice()))
                .andExpect(jsonPath("$.paid").value(expected.isPaid()));
    }

    @Test
    void deleteOrder() throws Exception {
        mockMvc.perform(delete("/orders/{id}", 5)
                        .header("Authorization", "Bearer " + TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        mockMvc.perform(get("/orders/{id}", 5)
                        .header("Authorization", "Bearer " + TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("error").value("No value present"));
    }
}