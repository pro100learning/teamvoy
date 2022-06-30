package com.teamvoy.shop.service;

import com.teamvoy.shop.dto.AuthenticationDTO;
import com.teamvoy.shop.dto.UserCreateDTO;
import com.teamvoy.shop.dto.UserDTO;
import com.teamvoy.shop.dto.UserUpdateDTO;

import java.util.Map;

public interface UserService {

    boolean create(UserCreateDTO dto);

    UserDTO getById(Long userId);

    UserDTO update(UserUpdateDTO dto);

    boolean delete(Long userId);


    Map<String, String> login(AuthenticationDTO dto);
}
