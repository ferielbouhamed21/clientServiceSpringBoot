package com.example.demo.mappers;

import com.example.demo.dto.TicketCreatedDto;
import com.example.demo.dto.TicketResponseDto;
import com.example.demo.models.TicketsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketMapper1 extends AbstractMapper<TicketResponseDto,TicketCreatedDto,TicketsEntity> {

}
