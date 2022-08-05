package com.example.demo.services.impl;

import com.example.demo.dao.TicketRepository;
import com.example.demo.dto.TicketCreatedDto;
import com.example.demo.dto.TicketResponseDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mappers.TicketMapper1;
import com.example.demo.models.TicketsEntity;
import com.example.demo.services.facade.TicketService;
import com.example.demo.services.facade.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service()
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketMapper1 ticketMapper;
    private TicketRepository ticketRepository;
    @Autowired
    private ZohoDeskService zohoDeskService;
    @Autowired
    private UserService userService;

    public TicketServiceImpl(TicketRepository ticketRepository) {

        this.ticketRepository = ticketRepository;
    }

    @Override
    public String save (TicketCreatedDto ticketCreatedDto) throws Exception{
        Map<String, Object> map = new HashMap<>();
        map.put("subject", ticketCreatedDto.getSubject());
        map.put("departmentId", "753510000000006907");
        map.put("email", ticketCreatedDto.getEmail());
        map.put("phone", ticketCreatedDto.getPhone());
        map.put("contactId", "753510000000207029");
        map.put("description", ticketCreatedDto.getDescription());
        map.put("status", "Open");
        map.put("classification", ticketCreatedDto.getClassification());
        map.put("category", ticketCreatedDto.getCategory());
        map.put("language", ticketCreatedDto.getLanguage());
        map.put("assigneeId","753510000000139001");
        ticketRepository.save(ticketMapper.toNewEntity(ticketCreatedDto));
        return (this.zohoDeskService.createTicket(map));
        //return ticketMapper.toDto(ticket);
        // add the userId to the ticket
     //   ticket.setUser(ticketControllerDto.get());
    }

    @Override
    public TicketResponseDto findById(Integer id) {
        TicketsEntity ticket = ticketRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
        return ticketMapper.toDto(ticket);
    }



    @Override
    public void delete(Integer id){

        ticketRepository.deleteById(id);
    }

    @Override
    public TicketResponseDto update(TicketCreatedDto ticketCreatedDto, Integer id) throws ChangeSetPersister.NotFoundException{

        Optional<TicketsEntity> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            TicketsEntity newTicket = ticketMapper.toNewEntity(ticketCreatedDto);
            newTicket.setId(id);
            newTicket.setCreationDate(ticket.get().getCreationDate());
            newTicket.setLastModifiedDate(ticket.get().getLastModifiedDate());
            TicketsEntity updated = ticketRepository.save(newTicket);
            return ticketMapper.toDto(updated);
        } else {
            throw new EntityNotFoundException("Ticket Not Found");
        }
    }

    @Override
    public List<TicketResponseDto> findAll(){
        return ticketRepository.findAll()
                .stream().map(el -> ticketMapper.toDto(el))
                .collect(Collectors.toList());
    }

    @Override
   public List<TicketResponseDto> findByUser(Integer id){
        Iterable<TicketsEntity> tickets = ticketRepository.findByUserNative(id);
        List<TicketResponseDto> ticketResponseDtoList = new ArrayList<>();
        for (TicketsEntity ticket:tickets)
        {
            ticketResponseDtoList.add(ticketMapper.toDto(ticket)) ;
        }
        return ticketResponseDtoList;
   }

    @Override
    //get ticket by status
    public List<TicketResponseDto> findByStatus(String status){
        Iterable<TicketsEntity> tickets = ticketRepository.findByStatusNative(status);
        List<TicketResponseDto> ticketResponseDtoList = new ArrayList<>();
        for (TicketsEntity ticket:tickets)
        {
            ticketResponseDtoList.add(ticketMapper.toDto(ticket)) ;
        }
        return ticketResponseDtoList;
    }
}
