package com.example.demo.services.impl;

import com.example.demo.dao.UserRepository;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.dto.UserSignUpDto;
import com.example.demo.mappers.UserMapper1;
import com.example.demo.models.User;
import com.example.demo.services.facade.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service()
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper1 userMapper;
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponseDto save(UserSignUpDto userSignUpDto) {
        User user = userRepository.save(userMapper.toNewEntity(userSignUpDto));
        return userMapper.toDto(user);
    }


    @Override
    public UserResponseDto findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return userMapper.toDto(user);

    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll()
                .stream().map(el -> userMapper.toDto(el))
                .collect(Collectors.toList());
    }


}
