package com.example.springbootpetproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tickets_count")
@Getter
@Setter
@NoArgsConstructor
public class NumberOfPurchasedTicketsTypes extends BaseEntity implements Serializable {

    @Column(name = "count_of_purchased_tickets")
    private int countOfPurchasedTickets;

    @ManyToOne
    @JoinColumn(name = "ticket_type_id")
    private TicketType ticketType;

    @ManyToOne
    @JoinColumn(name = "purchased_tickets_id")
    private Order order;

}
