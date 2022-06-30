package com.teamvoy.shop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamvoy.shop.dto.UserDTO;
import com.teamvoy.shop.dto.UserUpdateDTO;
import com.teamvoy.shop.util.TokenGeneratorService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
@WithMockUser(username = "test2@gmail.com", password = "$2a$10$aNwQFNT/GxmWNYaG9CZ4BuoMh65OdysRNie5gJqHxyzHwkrx7gB/6", roles = {"USER", "MODERATOR"})
class UserControllerTest {

    private final String TOKEN = TokenGeneratorService.createToken("test2@gmail.com");

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void details() throws Exception {
        UserDTO expected = new UserDTO().toBuilder()
                .id(2L)
                .surname("Татарчук")
                .name("Андрій")
                .email("test2@gmail.com")
                .phone("380972553992")
                .build();

        mockMvc.perform(get("/user")
                        .header("Authorization", "Bearer " + TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.surname").value(expected.getSurname()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.email").value(expected.getEmail()))
                .andExpect(jsonPath("$.phone").value(expected.getPhone()));
    }

    @Test
    void updateUser() throws Exception {
        UserUpdateDTO user = new UserUpdateDTO().toBuilder()
                .id(2L)
                .surname("Богдан")
                .name("Ткачук")
                .email("test2@gmail.com")
                .phone("380972553993")
                .build();

        mockMvc.perform(put("/user")
                        .header("Authorization", "Bearer " + TOKEN)
                        .content(objectMapper.writeValueAsBytes(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.surname").value(user.getSurname()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.phone").value(user.getPhone()));
    }

    @Test
    void updateUserWithBusyData() throws Exception {
        UserUpdateDTO user = new UserUpdateDTO().toBuilder()
                .id(2L)
                .surname("Богдан")
                .name("Ткачук")
                .email("test1@gmail.com")
                .phone("380972553991")
                .build();

        mockMvc.perform(put("/user")
                        .header("Authorization", "Bearer " + TOKEN)
                        .content(objectMapper.writeValueAsBytes(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("error").value("This email is busy"));

        user.setEmail("test2@gmail.com");

        mockMvc.perform(put("/user")
                        .header("Authorization", "Bearer " + TOKEN)
                        .content(objectMapper.writeValueAsBytes(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("error").value("This phone is busy"));
    }

    @Test
    void updateUserWithNewEmail() throws Exception {
        UserUpdateDTO user = new UserUpdateDTO().toBuilder()
                .id(2L)
                .surname("Андрій")
                .name("Татарчук")
                .email("test3@gmail.com")
                .phone("380972553992")
                .build();

        mockMvc.perform(put("/user")
                        .header("Authorization", "Bearer " + TOKEN)
                        .content(objectMapper.writeValueAsBytes(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("error").value("Log in again"));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/user")
                        .header("Authorization", "Bearer " + TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}