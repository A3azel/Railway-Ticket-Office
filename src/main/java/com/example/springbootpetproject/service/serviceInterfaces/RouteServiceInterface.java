package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.customExceptions.cityExceptions.InvalidNameOfCity;
import com.example.springbootpetproject.customExceptions.routeExceptions.DataCompareError;
import com.example.springbootpetproject.customExceptions.routeExceptions.ProblemWithSeatsCount;
import com.example.springbootpetproject.customExceptions.routeExceptions.RouteNotFound;
import com.example.springbootpetproject.customExceptions.stationExceptions.StationNotFound;
import com.example.springbootpetproject.dto.RouteDTO;
import com.example.springbootpetproject.entity.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface RouteServiceInterface {
    /*void addRoute(String trainNumber, String startStation, String arrivalStation, LocalDate dateOfDispatch, LocalDate dateOfArrival
            , LocalTime timeOfDispatch, LocalTime timeOfArrival, LocalTime travelTime, int numberOfCompartmentSeats
            , int numberOfSuiteSeats, BigDecimal priseOfCompartmentTicket, BigDecimal priseOfSuiteTicket);*/
    Map<String,String> addRoute(RouteDTO routeDTO) throws StationNotFound, DataCompareError, ProblemWithSeatsCount;
    void updateRoute(Map<String,String> allParam);
    void deleteRoute(Long Id);
    Route findRouteById(Long Id);
    Route findById(Long Id) throws RouteNotFound;
    Page<RouteDTO> getAllWayBetweenCitiesWithTime(String senderCity, String cityOfArrival,  String selectedDatesString, String selectedTimeString, Pageable pageable, int pageNumber, String direction, String sort) throws InvalidNameOfCity, RouteNotFound;
    Page<RouteDTO> getAll(Pageable pageable, int pageNumber, String direction, String sort);
    void reduceNumberOfCompartmentFreeSeats(Long routeId, int countOfPurchasedTickets);
    void reduceNumberOfSuiteFreeSeats(Long routeId, int countOfPurchasedTickets);
    RouteDTO convertRouteToRouteDTO(Route route);
}
