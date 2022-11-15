package com.example.springbootpetproject.facade;

import com.example.springbootpetproject.dto.RouteDTO;
import com.example.springbootpetproject.entity.Route;
import com.example.springbootpetproject.entity.Station;
import org.springframework.stereotype.Component;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RouteFacade {

    public RouteDTO convertRouteToRouteDTO(Route route){
        RouteDTO routeDTO = new RouteDTO();
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        routeDTO.setId(route.getId());
        routeDTO.setCreated(formatter.format(route.getCreated()));
        routeDTO.setUpdated(formatter.format(route.getUpdated()));
        routeDTO.setCreatedBy(route.getCreatedBy());
        routeDTO.setLastModifiedBy(route.getLastModifiedBy());
        routeDTO.setStartStationName(route.getStartStation().getStationName());
        routeDTO.setStartCityName(route.getStartStation().getCity().getCityName());
        routeDTO.setDepartureTime(route.getDepartureTime());
        routeDTO.setArrivalStationName(route.getArrivalStation().getStationName());
        routeDTO.setArrivalCityName(route.getArrivalStation().getCity().getCityName());
        routeDTO.setTravelTime(route.getTravelTime());
        routeDTO.setArrivalTime(route.getArrivalTime());
        routeDTO.setNumberOfCompartmentFreeSeats(route.getNumberOfCompartmentFreeSeats());
        routeDTO.setNumberOfSuiteFreeSeats(route.getNumberOfSuiteFreeSeats());
        routeDTO.setPriseOfCompartmentTicket(route.getPriseOfCompartmentTicket());
        routeDTO.setPriseOfSuiteTicket(route.getPriseOfSuiteTicket());
        routeDTO.setTrainNumber(route.getTrain().getTrainNumber());
        routeDTO.setNumberOfCompartmentSeats(route.getTrain().getNumberOfCompartmentSeats());
        routeDTO.setNumberOfSuiteSeats(route.getTrain().getNumberOfSuiteSeats());
        List<String> intermediateStations = route.getStationList().stream()
                .map(Station::getStationName)
                .collect(Collectors.toList());
        routeDTO.setIntermediateStations(intermediateStations);
        return routeDTO;
    }
}
