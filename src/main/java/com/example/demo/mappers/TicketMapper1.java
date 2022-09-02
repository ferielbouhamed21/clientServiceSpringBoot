package com.example.demo.mappers;

import com.example.demo.dto.TicketCreatedDto;
import com.example.demo.dto.TicketResponseDto;
import com.example.demo.models.TicketsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper1 extends AbstractMapper<TicketResponseDto, TicketCreatedDto, TicketsEntity> {

}
