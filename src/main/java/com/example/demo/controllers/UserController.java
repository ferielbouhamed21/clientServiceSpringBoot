package com.example.demo.controllers;

import com.example.demo.dto.UserHolder;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.dto.UserSignUpDto;
import com.example.demo.services.facade.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    UserHolder user;

    @GetMapping("")
    // @RolesAllowed("admin")
    public List<UserResponseDto> getClients() {
        return userService.findAll();
    }


    @PostMapping("")
    @RolesAllowed({"user", "admin"})
    public UserResponseDto save(
            @RequestBody() UserSignUpDto userSignUpDto) {
        return userService.save(userSignUpDto);
    }


    @GetMapping("/username")
    @RolesAllowed("user")
    public UserResponseDto findByUsername() {
        return userService.findByUsername(user.getUsername());
    }


}
