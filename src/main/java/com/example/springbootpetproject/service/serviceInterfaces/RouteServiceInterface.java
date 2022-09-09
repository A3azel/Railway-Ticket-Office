package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.entity.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

public interface RouteServiceInterface {
    /*void addRoute(String trainNumber, String startStation, String arrivalStation, LocalDate dateOfDispatch, LocalDate dateOfArrival
            , LocalTime timeOfDispatch, LocalTime timeOfArrival, LocalTime travelTime, int numberOfCompartmentSeats
            , int numberOfSuiteSeats, BigDecimal priseOfCompartmentTicket, BigDecimal priseOfSuiteTicket);*/
    void addRoute(Map<String,String> allParam);
    void updateRoute(Map<String,String> allParam);
    void deleteRoute(Long Id);
    Route findById(Long Id);
    Page<Route> getAllWayBetweenCitiesWithTime(String senderCity, String cityOfArrival,  String selectedDatesString, String selectedTimeString, Pageable pageable, int pageNumber, String direction, String sort);
    Page<Route> getAll(Pageable pageable, int pageNumber, String direction, String sort);
    void reduceNumberOfCompartmentFreeSeats(Long routeId, int countOfPurchasedTickets);
    void reduceNumberOfSuiteFreeSeats(Long routeId, int countOfPurchasedTickets);
}
