package com.teamvoy.shop.mapper;

import com.teamvoy.shop.dto.UserCreateDTO;
import com.teamvoy.shop.dto.UserDTO;
import com.teamvoy.shop.dto.UserUpdateDTO;
import com.teamvoy.shop.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    User toEntity(UserCreateDTO dto);

    User toEntity(UserUpdateDTO dto);


    UserDTO toUserDTO(User user);

    List<UserDTO> toUserDTO(List<User> users);
}
