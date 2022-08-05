package com.example.demo.services.impl;

import com.example.demo.dao.UserRepository;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.dto.UserSignUpDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mappers.UserMapper1;
import com.example.demo.models.User;
import com.example.demo.services.facade.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

@Service()
public class UserServiceImpl implements UserService {
   @Autowired
   private UserMapper1 userMapper;
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponseDto save (UserSignUpDto userSignUpDto) {
            User user = userRepository.save(userMapper.toNewEntity(userSignUpDto));
            return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto findById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toDto(user);

    }

    @Override
    public UserResponseDto findByUsername(String username){
        User user = userRepository.findByUsername(username);
        return userMapper.toDto(user);

    }

    @Override
    public void delete(Integer id){
          userRepository.deleteById(id);
    }

    @Override
    public UserResponseDto update(UserSignUpDto userSignUpDto, Integer id) throws ChangeSetPersister.NotFoundException{

       Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User newUser = userMapper.toNewEntity(userSignUpDto);
            newUser.setId(id);
            newUser.setCreationDate(user.get().getCreationDate());
            newUser.setLastModifiedDate(user.get().getLastModifiedDate());
            User updated = userRepository.save(newUser);
            return userMapper.toDto(updated);
        } else {
            throw new EntityNotFoundException("User Not Found");
        }
    }

    @Override
        public List<UserResponseDto> findAll(){
        return userRepository.findAll()
                .stream().map(el -> userMapper.toDto(el))
                .collect(Collectors.toList());
    }


}
