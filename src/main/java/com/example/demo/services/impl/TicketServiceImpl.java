package com.example.demo.services.impl;

import com.example.demo.dao.TicketRepository;
import com.example.demo.dto.TicketCreatedDto;
import com.example.demo.dto.TicketResponseDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mappers.TicketMapper1;
import com.example.demo.mappers.UserMapper1;
import com.example.demo.models.TicketsEntity;
import com.example.demo.models.User;
import com.example.demo.services.facade.TicketService;
import com.example.demo.services.facade.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service()
@Configuration()
@Component()
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketMapper1 ticketMapper;
    @Autowired
    private UserMapper1 userMapper1;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ZohoDeskService zohoDeskService;
    @Autowired
    private UserService userService;

    private String assigneeId = "753510000000139001";

    private String departmentId = "753510000000006907";

    private String status = "Open";

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Object save(TicketCreatedDto ticketCreatedDto, String username) throws Exception {
        Map<String, Object> map = new HashMap<>();
        UserResponseDto user = userService.findByUsername(username);
        User getUser = userMapper1.toEntity(user);
        System.out.println(getUser);
        ticketCreatedDto.setUser(getUser);
        ticketCreatedDto.setDepartmentId(departmentId);
        ticketCreatedDto.setAssigneeId(assigneeId);
        ticketCreatedDto.setStatus(status);
        System.out.println(getUser.getContactId());

        map.put("subject", ticketCreatedDto.getSubject());
        map.put("departmentId", departmentId);
        map.put("email", getUser.getEmail());
        map.put("phone", getUser.getPhone());
        map.put("contactId", getUser.getContactId());
        map.put("description", ticketCreatedDto.getDescription());
        map.put("status", status);
        map.put("classification", ticketCreatedDto.getClassification());
        map.put("category", ticketCreatedDto.getCategory());
        map.put("language", ticketCreatedDto.getLanguage());
        map.put("assigneeId", assigneeId);

        String id = this.zohoDeskService.createTicket(map);
        ticketCreatedDto.setId(id);
        TicketsEntity newTicket = ticketRepository.save(ticketMapper.toNewEntity(ticketCreatedDto));

        map.put("id", id);
        return map;
    }

    @Override
    public TicketResponseDto findById(String id) {
        TicketsEntity ticket = ticketRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
        return ticketMapper.toDto(ticket);
    }

    @Override
    public void delete(String id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public TicketResponseDto update(TicketCreatedDto ticketCreatedDto, String id) {

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
    public List<TicketResponseDto> findAll(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<TicketsEntity> pagedResult = ticketRepository.findAll(paging);
        return pagedResult.getContent()
                .stream().map(el -> ticketMapper.toDto(el))
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketResponseDto> findByUser(String username, Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<TicketsEntity> tickets = ticketRepository.findByUserNative(username, paging);
        List<TicketResponseDto> ticketResponseDtoList = new ArrayList<>();
        for (TicketsEntity ticket : tickets) {
            ticketResponseDtoList.add(ticketMapper.toDto(ticket));
        }
        return ticketResponseDtoList;
    }

    @Override
    public List<TicketResponseDto> findByStatus(String status) {
        Iterable<TicketsEntity> tickets = ticketRepository.findByStatusNative(status);
        List<TicketResponseDto> ticketResponseDtoList = new ArrayList<>();
        for (TicketsEntity ticket : tickets) {
            ticketResponseDtoList.add(ticketMapper.toDto(ticket));
        }
        return ticketResponseDtoList;
    }

    @Override
    public void updateStatus(String id, String status) {
        Optional<TicketsEntity> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            TicketsEntity newTicket = ticket.get();
            newTicket.setStatus(status);
            newTicket.setLastModifiedDate(Instant.now());
            ticketRepository.save(newTicket);
        }
    }

    @Override
    //get the tickets every hour and update the status of the tickets
    @Scheduled(fixedRate = 60  * 1000)
    public void cronJosbSch() throws Exception {
        System.out.println("cron job");
        JsonNode arrayNode = this.zohoDeskService.getAllTickets();
        System.out.println(arrayNode);

        JsonNode node = arrayNode.get("data");
        for (JsonNode jsonNode : node) {
            String id = jsonNode.get("id").asText();
            String status = jsonNode.get("status").asText();
            updateStatus(id, status);
        }

    }
}
