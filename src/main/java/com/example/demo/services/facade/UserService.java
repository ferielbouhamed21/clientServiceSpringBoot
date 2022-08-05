package com.example.demo.services.facade;

import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.dto.UserSignUpDto;
import com.example.demo.models.User;
import org.springframework.data.crossstore.ChangeSetPersister;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {

    UserResponseDto save (UserSignUpDto userSignUpDto);

    UserResponseDto findById(Integer id);

    UserResponseDto findByUsername(String username);

    void delete(Integer id);

    UserResponseDto update(UserSignUpDto userSignUpDto, Integer id) throws ChangeSetPersister.NotFoundException;

    List<UserResponseDto> findAll();

}

