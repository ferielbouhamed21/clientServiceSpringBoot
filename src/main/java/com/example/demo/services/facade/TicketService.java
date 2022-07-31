package com.example.demo.services.facade;

import com.example.demo.dto.TicketControllerDto;
import com.example.demo.dto.TicketCreatedDto;
import com.example.demo.dto.TicketResponseDto;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TicketService {

    TicketResponseDto save (TicketControllerDto ticketControllerDto) throws Exception;

    TicketResponseDto findById(Integer id);

    List<TicketResponseDto> findByUser(Integer id);

    void delete(Integer id);

    TicketResponseDto update(TicketCreatedDto ticketCreatedDto, Integer id) throws ChangeSetPersister.NotFoundException;

    List<TicketResponseDto> findAll();

}
