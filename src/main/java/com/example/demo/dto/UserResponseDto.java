package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

public class UserResponseDto {

    @JsonProperty("id")
    @NotNull
    private Integer id;

    @NotNull
    @JsonProperty("username")
    private String username;

}
