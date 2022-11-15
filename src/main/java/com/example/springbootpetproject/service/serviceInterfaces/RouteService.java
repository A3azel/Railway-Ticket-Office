package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.customExceptions.cityExceptions.InvalidNameOfCity;
import com.example.springbootpetproject.customExceptions.routeExceptions.RouteNotFound;
import com.example.springbootpetproject.dto.RouteDTO;
import com.example.springbootpetproject.entity.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface RouteService {
    Map<String,String> addRoute(RouteDTO routeDTO);
    Map<String,String> updateRoute(RouteDTO routeDTO, Long id);
    void deleteRoute(Long Id);
    Route findRouteById(Long Id);
    Route findById(Long Id) throws RouteNotFound;
    Page<RouteDTO> getAllWayBetweenCitiesWithTime(String senderCity, String cityOfArrival,  String selectedDatesString, String selectedTimeString, Pageable pageable, int pageNumber, String direction, String sort) throws InvalidNameOfCity, RouteNotFound;
    Page<RouteDTO> getAll(Pageable pageable, int pageNumber, String direction, String sort);
    void reduceNumberOfCompartmentFreeSeats(Long routeId, int countOfPurchasedTickets);
    void reduceNumberOfSuiteFreeSeats(Long routeId, int countOfPurchasedTickets);
}
