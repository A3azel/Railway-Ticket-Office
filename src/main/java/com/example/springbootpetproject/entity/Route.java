package com.example.springbootpetproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "route")
@Getter
@Setter
@NoArgsConstructor
public class Route extends BaseEntity implements Serializable {

    @OneToOne
    @JoinColumn(name = "start_station_id")
    private Station startStation;

    @NotBlank
    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @NotBlank
    @Column(name = "travel_time")
    private LocalTime travelTime;

    @OneToOne
    @JoinColumn(name = "arrival_station_id")
    private Station arrivalStation;

    @NotBlank
    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Min(value = 0 , message = "Count of compartment free seats can't negative")
    @Column(name = "number_of_compartment_free_seats")
    private int numberOfCompartmentFreeSeats;

    @NotBlank
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Min(value = 0 , message = "Count of suite free seats can't negative")
    @Column(name = "number_of_suite_free_seats")
    private int numberOfSuiteFreeSeats;

    @NotBlank
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Min(value = 0 , message = "Count of compartment seats can't negative")
    @Column(name = "prise_of_compartment_ticket")
    private BigDecimal priseOfCompartmentTicket;

    @NotBlank
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Min(value = 0 , message = "Count of suite seats can't negative")
    @Column(name = "prise_of_suite_ticket")
    private BigDecimal priseOfSuiteTicket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id")
    @JsonBackReference
    private Train train;
}
