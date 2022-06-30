package com.teamvoy.shop.service;

import com.teamvoy.shop.dto.UserCreateDTO;
import com.teamvoy.shop.dto.UserDTO;
import com.teamvoy.shop.dto.UserUpdateDTO;
import com.teamvoy.shop.entity.User;
import com.teamvoy.shop.mapper.UserMapper;
import com.teamvoy.shop.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private PasswordEncoder passwordEncoder;


    @Test
    void createTest() {
        UserCreateDTO dto = new UserCreateDTO().toBuilder()
                .surname("Богдан")
                .name("Ткачук")
                .email("bt769271@gmail.com")
                .phone("380972553991")
                .password("12345678")
                .build();

        User user = new User().toBuilder()
                .surname("Богдан")
                .name("Ткачук")
                .email("bt769271@gmail.com")
                .phone("380972553991")
                .password("12345678")
                .build();

        when(userMapper.toEntity(dto)).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());

        assertTrue(userService.create(dto));

        verify(userRepository, times(1)).findByEmail(dto.getEmail());
        verify(userRepository, times(1)).findByPhone(dto.getPhone());
        verify(userMapper, times(1)).toEntity(dto);
        verify(passwordEncoder, times(1)).encode(user.getPassword());
        verify(userRepository, times(1)).save(user);

    }

    @Test
    void getByIdTest() {
        User user = new User().toBuilder()
                .id(1L)
                .surname("Богдан")
                .name("Ткачук")
                .email("bt769271@gmail.com")
                .phone("380972553991")
                .password("12345678")
                .build();

        UserDTO dto = new UserDTO().toBuilder()
                .id(1L)
                .surname("Богдан")
                .name("Ткачук")
                .email("bt769271@gmail.com")
                .phone("380972553991")
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toUserDTO(user)).thenReturn(dto);

        assertEquals(dto, userService.getById(user.getId()));

        verify(userRepository, times(1)).findById(user.getId());
        verify(userMapper, times(1)).toUserDTO(user);
    }

    @Test
    void updateTest() {
        User oldUser = new User().toBuilder()
                .id(1L)
                .surname("Богдан")
                .name("Ткачук")
                .email("bt769271@gmail.com")
                .phone("380972553991")
                .password("12345678")
                .build();

        UserUpdateDTO userUpdateDto = new UserUpdateDTO().toBuilder()
                .id(1L)
                .surname("Андрій")
                .name("Татарчук")
                .email("bt769271@gmail.com")
                .phone("380972553992")
                .build();

        User newUser = new User().toBuilder()
                .id(1L)
                .surname("Андрій")
                .name("Татарчук")
                .email("bt769271@gmail.com")
                .phone("380972553992")
                .password("12345678")
                .build();

        UserDTO expected = new UserDTO().toBuilder()
                .id(1L)
                .surname("Андрій")
                .name("Татарчук")
                .email("bt769271@gmail.com")
                .phone("380972553992")
                .build();

        when(userRepository.findById(userUpdateDto.getId())).thenReturn(Optional.of(oldUser));
        when(userRepository.save(newUser)).thenReturn(newUser);
        when(userMapper.toUserDTO(newUser)).thenReturn(expected);

        UserDTO actual = userService.update(userUpdateDto);

        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(oldUser.getId());
        verify(userRepository, times(1)).save(newUser);
        verify(userMapper, times(1)).toUserDTO(newUser);
    }

    @Test
    void delete() {
        User user = new User().toBuilder()
                .id(1L)
                .surname("Богдан")
                .name("Ткачук")
                .email("bt769271@gmail.com")
                .phone("380972553991")
                .password("12345678")
                .enabled(true)
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        assertTrue(userService.delete(user.getId()));
        assertFalse(user.isEnabled());

        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(user);
    }
}