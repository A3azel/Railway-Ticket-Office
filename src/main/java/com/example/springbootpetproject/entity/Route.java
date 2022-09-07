package com.example.springbootpetproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "route")
@Getter
@Setter
@NoArgsConstructor
public class Route implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @OneToOne
    @JoinColumn(name = "start_station_id")
    private Station startStation;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "travel_time", nullable = false)
    private LocalTime travelTime;

    @OneToOne
    @JoinColumn(name = "arrival_station_id")
    private Station arrivalStation;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "number_of_compartment_free_seats")
    private int numberOfCompartmentFreeSeats;

    @Column(name = "number_of_suite_free_seats")
    private int numberOfSuiteFreeSeats;

    @Column(name = "prise_of_compartment_ticket", nullable = false)
    private BigDecimal priseOfCompartmentTicket;

    @Column(name = "prise_of_suite_ticket", nullable = false)
    private BigDecimal priseOfSuiteTicket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id")
    @JsonBackReference
    private Train train;

    public Route(Station startStation, LocalDateTime departureTime, LocalTime travelTime, Station arrivalStation, LocalDateTime arrivalTime, int numberOfCompartmentFreeSeats, int numberOfSuiteFreeSeats, BigDecimal priseOfCompartmentTicket, BigDecimal priseOfSuiteTicket, Train train) {
        this.startStation = startStation;
        this.departureTime = departureTime;
        this.travelTime = travelTime;
        this.arrivalStation = arrivalStation;
        this.arrivalTime = arrivalTime;
        this.numberOfCompartmentFreeSeats = numberOfCompartmentFreeSeats;
        this.numberOfSuiteFreeSeats = numberOfSuiteFreeSeats;
        this.priseOfCompartmentTicket = priseOfCompartmentTicket;
        this.priseOfSuiteTicket = priseOfSuiteTicket;
        this.train = train;
    }
}
