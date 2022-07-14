package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

public class TicketResponseDto {

    @JsonProperty("id")
    @NotNull
    private Integer id;
}
