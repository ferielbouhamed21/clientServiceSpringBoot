package com.example.demo.controllers;

import com.example.demo.dto.TicketCreatedDto;
import com.example.demo.dto.TicketResponseDto;
import com.example.demo.services.facade.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("ticket")

public class TicketController {

    @Autowired
    private TicketService ticketService;


        @GetMapping("")
        @RolesAllowed("admin")
        public List<TicketResponseDto> getTickets() {

            return ticketService.findAll();
        }


        @PostMapping("")
        @RolesAllowed({"user","admin"})
        public TicketResponseDto save(
                @RequestBody() TicketCreatedDto ticketCreatedDto) {
            System.out.println(ticketCreatedDto.getDepartmentId());
            return ticketService.save(ticketCreatedDto);
        }

        @GetMapping("/{id}")
        @RolesAllowed({"user"})
        public TicketResponseDto findById(@PathVariable("id") Integer id) {

            return ticketService.findById(id);
        }


        @GetMapping("/user/{id}")
        @RolesAllowed({"user","admin"})
        public List<TicketResponseDto> findByUser (@PathVariable() Integer id) {
            return ticketService.findByUser(id);
        }

        @DeleteMapping("/{id}")
        @RolesAllowed("user")
        public void delete(@PathVariable() Integer id) {
            ticketService.delete(id);
        }



        @PutMapping("/{id}")
        @RolesAllowed("user")
        public TicketResponseDto update(@RequestBody() TicketCreatedDto ticketCreatedDto, @PathVariable() Integer id) throws ChangeSetPersister.NotFoundException {
            return ticketService.update(ticketCreatedDto, id);
        }
    }
