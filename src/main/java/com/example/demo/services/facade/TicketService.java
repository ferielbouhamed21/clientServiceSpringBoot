package com.example.demo.services.facade;

import com.example.demo.dto.TicketCreatedDto;
import com.example.demo.dto.TicketResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.crossstore.ChangeSetPersister;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface TicketService {

        String save (TicketCreatedDto ticketCreatedDto) throws Exception;

    TicketResponseDto findById(Integer id);

    List<TicketResponseDto> findByUser(Integer id);

    void delete(Integer id);

    TicketResponseDto update(TicketCreatedDto ticketCreatedDto, Integer id) throws ChangeSetPersister.NotFoundException;

    List<TicketResponseDto> findAll();

    //get ticket by status
    List<TicketResponseDto> findByStatus(String status);

}
