package com.teamvoy.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamvoy.shop.dto.BasketDTO;
import com.teamvoy.shop.dto.BasketUpdateDTO;
import com.teamvoy.shop.dto.PhoneDTOWithOtherInfo;
import com.teamvoy.shop.dto.UserDTO;
import com.teamvoy.shop.util.TokenGeneratorService;
import org.junit.Before;
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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
@Sql(value = {"/create-basket-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = {"/create-phone-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithMockUser(username = "test2@gmail.com", password = "$2a$10$aNwQFNT/GxmWNYaG9CZ4BuoMh65OdysRNie5gJqHxyzHwkrx7gB/6", roles = {"USER", "MODERATOR"})
class BasketControllerTest {

    private final String TOKEN = TokenGeneratorService.createToken("test2@gmail.com");

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private UserDTO user;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();

        user = new UserDTO().toBuilder()
                .id(2L)
                .surname("Татарчук")
                .name("Андрій")
                .email("test2@gmail.com")
                .phone("380972553992")
                .build();
    }

    @Test
    void details() throws Exception {
        BasketDTO expected = new BasketDTO().toBuilder()
                .id(2L)
                .user(user)
                .price(2700)
                .build();

        mockMvc.perform(get("/basket")
                        .header("Authorization", "Bearer " + TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.user").value(expected.getUser()))
                .andExpect(jsonPath("$.price").value(expected.getPrice()));
    }

    @Test
    void addToBasket() throws Exception {
        BasketUpdateDTO addToBasket = new BasketUpdateDTO().toBuilder()
                .phoneId(4L)
                .isAdd(true)
                .amount(3)
                .build();

        BasketDTO expected = new BasketDTO().toBuilder()
                .id(2L)
                .user(user)
                .price(3600)
                .build();

        mockMvc.perform(put("/basket")
                        .header("Authorization", "Bearer " + TOKEN)
                        .content(objectMapper.writeValueAsBytes(addToBasket))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.user").value(expected.getUser()))
                .andExpect(jsonPath("$.price").value(expected.getPrice()));
    }

    @Test
    void removeFromBasket() throws Exception {
        BasketUpdateDTO removeFromBasket = new BasketUpdateDTO().toBuilder()
                .phoneId(4L)
                .isAdd(false)
                .amount(1)
                .build();

        BasketDTO expected = new BasketDTO().toBuilder()
                .id(2L)
                .user(user)
                .price(2400)
                .build();

        mockMvc.perform(put("/basket")
                        .header("Authorization", "Bearer " + TOKEN)
                        .content(objectMapper.writeValueAsBytes(removeFromBasket))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.user").value(expected.getUser()))
                .andExpect(jsonPath("$.price").value(expected.getPrice()));
    }

    @Test
    void clear() throws Exception {
        mockMvc.perform(delete("/basket")
                        .header("Authorization", "Bearer " + TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        BasketDTO expected = new BasketDTO().toBuilder()
                .id(2L)
                .user(user)
                .price(0)
                .build();

        mockMvc.perform(get("/basket")
                        .header("Authorization", "Bearer " + TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.user").value(expected.getUser()))
                .andExpect(jsonPath("$.price").value(expected.getPrice()));
    }
}