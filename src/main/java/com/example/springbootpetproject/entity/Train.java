package com.example.springbootpetproject.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Table(name = "train_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Train implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "train_number", nullable = false)
    private String trainNumber;

    @Column(name = "number_of_compartment_seats")
    private int numberOfCompartmentSeats;

    @Column(name = "number_of_suite_seats")
    private int numberOfSuiteSeats;

    @OneToMany(mappedBy = "train")
    @JsonManagedReference
    private Set<Route> routeSet;
}
