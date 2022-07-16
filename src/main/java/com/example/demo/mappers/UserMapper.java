package com.example.demo.mappers;


import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.dto.UserSignUpDto;
import com.example.demo.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "username" , target = "username")
    User map (UserSignUpDto user);
    UserResponseDto map(User user);
    User map (UserRequestDto user);
    List<UserResponseDto> map (List<User> users);
}
