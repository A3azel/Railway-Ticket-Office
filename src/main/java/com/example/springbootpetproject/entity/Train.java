package com.example.springbootpetproject.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

/*@Entity
@Table(name = "train_info")
@Data
@NoArgsConstructor
*//*@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString*//*
public class Train implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "train_number", nullable = false)
    private String trainNumber;

    //@ManyToOne(fetch = FetchType.LAZY)
    @OneToOne
    @JoinColumn(name = "start_station_id")
    private Station startStation;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "travel_time", nullable = false)
    private LocalTime travelTime;

    //@ManyToOne(fetch = FetchType.LAZY)
    @OneToOne
    @JoinColumn(name = "arrival_station_id")
    private Station arrivalStation;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "number_of_free_seats", nullable = false)
    private int numberOfFreeSeats;

    @Column(name = "prise_of_ticket", nullable = false)
    private BigDecimal priseOfTicket;

}*/

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
