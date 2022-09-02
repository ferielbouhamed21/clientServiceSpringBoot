package com.example.demo.dao;

import com.example.demo.models.TicketsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository <TicketsEntity, String> {

    @Query(value = "SELECT t FROM  TicketsEntity t inner join t.user u WHERE u.username= :username",
            nativeQuery = false)
    public Page <TicketsEntity> findByUserNative( String username, Pageable pageable);

    //get ticket by status
    @Query(value = "SELECT * FROM tickets WHERE status = :status", nativeQuery = true)
    public Iterable<TicketsEntity> findByStatusNative(@Param("status") String status);
}
