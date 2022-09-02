package com.example.demo.services.facade;

import com.example.demo.dto.TicketCreatedDto;
import com.example.demo.dto.TicketResponseDto;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

public interface TicketService {

    Object save(TicketCreatedDto ticketCreatedDto, String username) throws Exception;

    TicketResponseDto findById(String id);

    List<TicketResponseDto> findByUser(String username, Integer pageNo, Integer pageSize);

    void delete(String id);

    TicketResponseDto update(TicketCreatedDto ticketCreatedDto, String id) throws ChangeSetPersister.NotFoundException;

    List<TicketResponseDto> findAll(Integer pageNo, Integer pageSize);

    //get ticket by status
    List<TicketResponseDto> findByStatus(String status);

    void cronJosbSch() throws Exception;

    void updateStatus(String id, String status);

}
