package com.teamvoy.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamvoy.shop.dto.AuthenticationDTO;
import com.teamvoy.shop.dto.UserCreateDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
public class AuthorizationControllerTest {

    @Autowired
    private MockMvc mvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void accessDeniedTest() throws Exception {
        mvc.perform(get("/user"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void incorrectLoginWithOtherDataTest() throws Exception {
        AuthenticationDTO loginUser = new AuthenticationDTO().toBuilder()
                .email("test1@gmail.com")
                .password("12345678")
                .build();

        mvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(loginUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("error").value("Bad credentials"));
    }

    @Test
    public void incorrectLoginTest() throws Exception {
        AuthenticationDTO loginUser = new AuthenticationDTO().toBuilder()
                .email("test3@@gmail.com")
                .password("12345678")
                .build();

        mvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(loginUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void incorrectDataLoginTest() throws Exception {
        AuthenticationDTO loginUser = new AuthenticationDTO().toBuilder()
                .email("test3")
                .password("12345678")
                .build();

        mvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(loginUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("errors").isNotEmpty());
    }

    @Test
    public void correctRegistrationTest() throws Exception {
        UserCreateDTO registrationUser = new UserCreateDTO().toBuilder()
                .surname("Богдан")
                .name("Ткачук")
                .email("bt769271@gmail.com")
                .phone("380972553993")
                .password("12345678")
                .build();

        mvc.perform(post("/registration")
                        .content(objectMapper.writeValueAsString(registrationUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void incorrectRegistrationWithSameDataTest() throws Exception {
        UserCreateDTO registrationUser = new UserCreateDTO().toBuilder()
                .surname("Богдан")
                .name("Ткачук")
                .email("test1@gmail.com")
                .phone("380972553991")
                .password("12345678")
                .build();

        mvc.perform(post("/registration")
                        .content(objectMapper.writeValueAsString(registrationUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("error").value("This email is busy"));

        registrationUser.setEmail("bogdan@gmail.com");

        mvc.perform(post("/registration")
                        .content(objectMapper.writeValueAsString(registrationUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("error").value("This phone is busy"));
    }

    @Test
    public void incorrectDataRegistrationTest() throws Exception {
        UserCreateDTO registrationUser = new UserCreateDTO().toBuilder()
                .surname("Богдан")
                .name("Ткачук")
                .email("test1")
                .phone("380972553991")
                .password("12345678")
                .build();

        mvc.perform(post("/registration")
                        .content(objectMapper.writeValueAsString(registrationUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("errors").isNotEmpty());

    }
}