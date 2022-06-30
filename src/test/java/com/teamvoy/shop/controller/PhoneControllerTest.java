package com.teamvoy.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamvoy.shop.dto.PhoneDTO;
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
@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = {"/create-phone-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-phone-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithMockUser(username = "test2@gmail.com", password = "$2a$10$aNwQFNT/GxmWNYaG9CZ4BuoMh65OdysRNie5gJqHxyzHwkrx7gB/6", roles = {"USER", "MODERATOR"})
class PhoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllPhones() throws Exception {
        mockMvc.perform(get("/phones"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    void details() throws Exception {
        PhoneDTO expected = new PhoneDTO().toBuilder()
                .id(1L)
                .brand("Apple")
                .model("iPhone 11")
                .price(600)
                .amount(23)
                .color("Black")
                .description("Nice phone")
                .build();

        mockMvc.perform(get("/phones/{id}", expected.getId()).content(objectMapper.writeValueAsBytes(expected))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.brand").value(expected.getBrand()))
                .andExpect(jsonPath("$.model").value(expected.getModel()))
                .andExpect(jsonPath("$.price").value(expected.getPrice()))
                .andExpect(jsonPath("$.amount").value(expected.getAmount()))
                .andExpect(jsonPath("$.color").value(expected.getColor()))
                .andExpect(jsonPath("$.description").value(expected.getDescription()));
    }

    @Test
    void create() throws Exception {
        PhoneDTO newPhone = new PhoneDTO().toBuilder()
                .brand("Apple")
                .model("iPhone 13 Pro")
                .price(999)
                .amount(20)
                .color("White")
                .description("Phone with three cameras")
                .build();

        mockMvc.perform(post("/phones").content(objectMapper.writeValueAsBytes(newPhone))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.brand").value(newPhone.getBrand()))
                .andExpect(jsonPath("$.model").value(newPhone.getModel()))
                .andExpect(jsonPath("$.price").value(newPhone.getPrice()))
                .andExpect(jsonPath("$.amount").value(newPhone.getAmount()))
                .andExpect(jsonPath("$.color").value(newPhone.getColor()))
                .andExpect(jsonPath("$.description").value(newPhone.getDescription()));
    }

    @Test
    void update() throws Exception {
        PhoneDTO updatePhone = new PhoneDTO().toBuilder()
                .id(5L)
                .brand("Apple")
                .model("iPhone 7")
                .price(150)
                .amount(45)
                .color("Black Jack")
                .description("My phone")
                .build();

        mockMvc.perform(put("/phones").content(objectMapper.writeValueAsBytes(updatePhone))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.brand").value(updatePhone.getBrand()))
                .andExpect(jsonPath("$.model").value(updatePhone.getModel()))
                .andExpect(jsonPath("$.price").value(updatePhone.getPrice()))
                .andExpect(jsonPath("$.amount").value(updatePhone.getAmount()))
                .andExpect(jsonPath("$.color").value(updatePhone.getColor()))
                .andExpect(jsonPath("$.description").value(updatePhone.getDescription()));
    }

    @Test
    void deletePhone() throws Exception {
        mockMvc.perform(get("/phones/count"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("count").value(5));

        mockMvc.perform(delete("/phones/{id}", 5))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        mockMvc.perform(get("/phones/count"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("count").value(4));
    }

    @Test
    void getPhonesCount() throws Exception {
        mockMvc.perform(get("/phones/count"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("count").value(5));
    }
}