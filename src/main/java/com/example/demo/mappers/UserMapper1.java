package com.example.demo.mappers;

import com.example.demo.dto.UserResponseDto;
import com.example.demo.dto.UserSignUpDto;
import com.example.demo.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper1 extends AbstractMapper<UserResponseDto, UserSignUpDto, User>{
}
