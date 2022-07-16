package com.example.demo.dao;

import com.example.demo.models.TicketsEntity;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository <TicketsEntity, Integer> {

    @Query(value = "SELECT * FROM tickets WHERE user_id in (select id from user where id = id)", nativeQuery = true)
    public Iterable<TicketsEntity> findByUserNative(@Param("id") Integer id);

}
