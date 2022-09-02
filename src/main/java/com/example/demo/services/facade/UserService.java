package com.example.demo.services.facade;

import com.example.demo.dto.UserResponseDto;
import com.example.demo.dto.UserSignUpDto;

import java.util.List;

public interface UserService {

    UserResponseDto save(UserSignUpDto userSignUpDto);


    UserResponseDto findByUsername(String username);


    List<UserResponseDto> findAll();

}

