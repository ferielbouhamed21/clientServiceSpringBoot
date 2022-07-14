package com.example.demo.mappers;


import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.models.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    UserRequestDto map(User user);
    User map (UserResponseDto user);
    List<User> map (List<UserResponseDto> users);
}
