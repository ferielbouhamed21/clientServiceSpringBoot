package com.example.demo.dao;

import com.example.demo.models.TicketsEntity;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository <User, Integer> {
}
