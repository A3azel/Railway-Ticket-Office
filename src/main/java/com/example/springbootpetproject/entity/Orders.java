package com.example.springbootpetproject.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    /*@OneToMany(mappedBy = "train_id")
    private Set<Train> trainSet;*/

    @OneToOne
    @JoinColumn(name = "train_id")
    private Train train;
}
