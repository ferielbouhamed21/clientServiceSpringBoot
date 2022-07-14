package com.example.demo.mappers;


import com.example.demo.dto.UserPostDto;
import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.models.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User map (UserPostDto user);
    UserResponseDto map(User user);
    User map (UserRequestDto user);
    List<UserResponseDto> map (List<User> users);
}
