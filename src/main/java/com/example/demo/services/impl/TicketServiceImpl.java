package com.example.demo.services.impl;

import com.example.demo.dao.TicketRepository;
import com.example.demo.dto.TicketCreatedDto;
import com.example.demo.dto.TicketResponseDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mappers.TicketMapper;
import com.example.demo.models.TicketsEntity;
import com.example.demo.services.facade.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service()
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketMapper ticketMapper;
    private TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {

        this.ticketRepository = ticketRepository;
    }

    @Override
    public TicketResponseDto save (TicketCreatedDto ticketCreatedDto) {
        TicketsEntity ticket = ticketRepository.save(ticketMapper.map(ticketCreatedDto));
        return ticketMapper.map(ticket);

    }

    @Override
    public TicketResponseDto findById(Integer id) {
        TicketsEntity ticket = ticketRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
        return ticketMapper.map(ticket);
    }



    @Override
    public void delete(Integer id){

        ticketRepository.deleteById(id);
    }

    @Override
    public TicketResponseDto update(TicketCreatedDto ticketCreatedDto, Integer id) throws ChangeSetPersister.NotFoundException{

        Optional<TicketsEntity> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            TicketsEntity newTicket = ticketMapper.map(ticketCreatedDto);
            newTicket.setId(id);
            newTicket.setCreationDate(ticket.get().getCreationDate());
            newTicket.setLastModifiedDate(ticket.get().getLastModifiedDate());
            TicketsEntity updated = ticketRepository.save(newTicket);
            return ticketMapper.map(updated);
        } else {
            throw new EntityNotFoundException("Ticket Not Found");
        }
    }

    @Override
    public List<TicketResponseDto> findAll(){
        return ticketRepository.findAll()
                .stream().map(el -> ticketMapper.map(el))
                .collect(Collectors.toList());
    }

    @Override
   public List<TicketResponseDto> findByUser(Integer id){
        Iterable<TicketsEntity> tickets = ticketRepository.findByUserNative(id);
        List<TicketResponseDto> ticketResponseDtoList = new ArrayList<>();
        for (TicketsEntity ticket:tickets)
        {
            ticketResponseDtoList.add(ticketMapper.map(ticket)) ;
        }
        return ticketResponseDtoList;
   }

}