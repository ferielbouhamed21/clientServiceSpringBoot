package com.example.demo.controllers;

import com.example.demo.dto.UserResponseDto;
import com.example.demo.dto.UserSignUpDto;
import com.example.demo.services.facade.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
  @Autowired
    private UserService userService;


    @GetMapping("")
   // @RolesAllowed("admin")
    public List<UserResponseDto> getClients() {
        return userService.findAll();
    }


    @PostMapping("")
    @RolesAllowed({"user","admin"})
    public UserResponseDto save(
             @RequestBody() UserSignUpDto userSignUpDto) {
        return userService.save(userSignUpDto);
    }

    @GetMapping("/{id}")
    //@RolesAllowed({"user","admin"})
    public UserResponseDto findById(@PathVariable("id") Integer id) {
             return userService.findById(id);
    }


    @GetMapping("/username/{username}")
    @RolesAllowed("user")
    public UserResponseDto findByUsername (@PathVariable() String username) {
        return userService.findByUsername(username);
    }

    @DeleteMapping("/{id}")
    @RolesAllowed("user")
    public void delete(@PathVariable() Integer id) {
        userService.delete(id);
    }



    @PutMapping("/{id}")
    @RolesAllowed("user")
    public UserResponseDto update(@RequestBody() UserSignUpDto userSignUpDto, @PathVariable() Integer id) throws ChangeSetPersister.NotFoundException {
        return userService.update(userSignUpDto, id);
    }
}
