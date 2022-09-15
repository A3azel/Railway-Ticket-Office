package com.example.springbootpetproject.dto;

import com.example.springbootpetproject.entity.Station;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteDTO {
    private Long id;
    private String startStationName;
    private String startCityName;
    private LocalDateTime departureTime;
    private LocalTime travelTime;
    private String arrivalStationName;
    private String arrivalCityName;
    private LocalDateTime arrivalTime;
   /* private LocalDate dateOfDispatch;
    private LocalTime timeOfDispatch;
    private LocalDate dateOfArrival;
    private LocalTime timeOfArrival;*/
    private int numberOfCompartmentFreeSeats;
    private int numberOfSuiteFreeSeats;
    private BigDecimal priseOfCompartmentTicket;
    private BigDecimal priseOfSuiteTicket;
    private String trainNumber;
    private int numberOfCompartmentSeats;
    private int numberOfSuiteSeats;

}
