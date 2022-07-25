package com.example.demo.services.impl;

import com.example.demo.dao.TicketRepository;
import com.example.demo.dto.TicketCreatedDto;
import com.example.demo.dto.TicketResponseDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mappers.TicketMapper1;
import com.example.demo.models.TicketsEntity;
import com.example.demo.services.facade.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service()
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketMapper1 ticketMapper;
    private TicketRepository ticketRepository;
    @Autowired
    private ZohoDeskService zohoDeskService;


    public TicketServiceImpl(TicketRepository ticketRepository) {

        this.ticketRepository = ticketRepository;
    }

    @Override
    public TicketResponseDto save (TicketCreatedDto ticketCreatedDto) throws Exception{
        Map<String, String> map = new HashMap<>();
        map.put("subject", ticketCreatedDto.getSubject());
        map.put("department", ticketCreatedDto.getDepartmentId());
        map.put("email", ticketCreatedDto.getEmail());
        map.put("phone", ticketCreatedDto.getPhone());
        map.put("contactId", "753510000000207029");
        this.zohoDeskService.createTicket(map);
        TicketsEntity ticket = ticketRepository.save(ticketMapper.toNewEntity(ticketCreatedDto));
        return ticketMapper.toDto(ticket);

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

}
