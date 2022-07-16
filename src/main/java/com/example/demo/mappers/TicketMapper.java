package com.example.demo.mappers;

import com.example.demo.dto.*;
import com.example.demo.models.TicketsEntity;
import com.example.demo.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    @Mapping(source = "id" , target = "id")
    TicketResponseDto map(TicketsEntity ticket);
    TicketsEntity map (TicketCreatedDto ticket);
    List<TicketResponseDto> map (List<TicketsEntity> tickets);

}
