package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name="tickets")

public class TicketsEntity extends AbstractEntity{
    @Id()
    private Integer ticketId;

    @ManyToOne()
    @JoinColumn(name="user_id")
    private User user;
}




