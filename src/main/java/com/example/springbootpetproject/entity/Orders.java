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
@Data
@NoArgsConstructor
public class Orders implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "orders_prise")
    private BigDecimal orderPrise;

    @Column(name = "order_time")
    private LocalDateTime orderTime;

    @Column(name = "count_of_purchased_tickets")
    private int countOfPurchasedTickets;

    //@OneToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    /*@OneToMany(mappedBy = "train_id")
    private Set<Train> trainSet;*/

    @OneToOne
    @JoinColumn(name = "train_id")
    private Train train;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_type_id")
    @JsonBackReference
    private TicketType ticketType;

}
