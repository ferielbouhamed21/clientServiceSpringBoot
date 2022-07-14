package com.example.demo.services;

import com.example.demo.dto.UserPostDto;
import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResponseDto;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

public interface UserService {

    UserResponseDto save (UserPostDto userPostDto);

    UserResponseDto findById(Integer id);

    UserResponseDto findByUsername(String username);

    void delete(Integer id);

    UserResponseDto update(UserRequestDto userRequestDto, Integer id) throws ChangeSetPersister.NotFoundException;

    List<UserResponseDto> findAll();
}

