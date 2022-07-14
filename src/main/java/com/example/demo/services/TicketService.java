package com.example.demo.services;

import com.example.demo.dto.TicketRequestDto;
import com.example.demo.dto.TicketResponseDto;
import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResponseDto;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

public interface TicketService {

    TicketResponseDto save (TicketRequestDto ticketRequestDto);

    TicketResponseDto findById(Integer id);

    TicketResponseDto findByUser(Integer id);

    void delete(Integer id);

    TicketResponseDto update(TicketRequestDto ticketRequestDto, Integer id) throws ChangeSetPersister.NotFoundException;

    List<TicketResponseDto> findAll();
}
