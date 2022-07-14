package com.example.demo.services;

import com.example.demo.dao.UserRepository;
import com.example.demo.dto.UserPostDto;
import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mappers.UserMapper;
import com.example.demo.mappers.UserMapperImpl;
import com.example.demo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

@Service()
public class UserServiceImpl implements UserService {
    @Autowired
    private  UserMapper userMapper;
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDto save (UserPostDto userPostDto){
        User user = userRepository.save(
                userMapper.map(userPostDto)
        );

        return userMapper.map(user);
    }

    @Override
    public UserResponseDto findById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Client not found"));
        return userMapper.map(user);
    }

    @Override
    public UserResponseDto findByUsername(String username){
        User user = userRepository.findByUsername(username);
        return userMapper.map(user);
    }

    @Override
    public void delete(Integer id){
          userRepository.deleteById(id);
    }

    @Override
    public UserResponseDto update(UserRequestDto userRequestDto, Integer id) throws ChangeSetPersister.NotFoundException{

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User newUser = userMapper.map(userRequestDto);
            newUser.setId(id);
            User updated = userRepository.save(newUser);
            return userMapper.map(updated);
        } else {
            throw new EntityNotFoundException("Client Not Found");
        }
    }

    @Override
    public List<UserResponseDto> findAll(){
        return userRepository.findAll()
                .stream().map(el -> userMapper.map(el))
                .collect(Collectors.toList());
    }

}
