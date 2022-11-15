package com.example.springbootpetproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "purchased_tickets")
@Getter
@Setter
@NoArgsConstructor
public class Order extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "orders_prise")
    private BigDecimal orderPrise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;

    @OneToMany(mappedBy = "order",fetch = FetchType.LAZY)
    private Set<NumberOfPurchasedTicketsTypes> numberOfPurchasedTicketsTypesSet;

/*
    @Column(name = "count_of_purchased_tickets")
    private int countOfPurchasedTickets;
*/


/*    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_type_id")
    @JsonBackReference
    private TicketType ticketType;*/



}
