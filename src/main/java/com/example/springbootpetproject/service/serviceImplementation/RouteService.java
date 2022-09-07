package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.entity.Route;
import com.example.springbootpetproject.entity.Station;
import com.example.springbootpetproject.entity.Train;
import com.example.springbootpetproject.repository.RouteRepository;
import com.example.springbootpetproject.service.serviceInterfaces.RouteServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@Service
public class RouteService implements RouteServiceInterface {
    private final RouteRepository routeRepository;
    private final TrainService trainService;
    private final StationService stationService;

    @Autowired
    public RouteService(RouteRepository routeRepository, TrainService trainService, StationService stationService) {
        this.routeRepository = routeRepository;
        this.trainService = trainService;
        this.stationService = stationService;
    }

    /*@Override
    @Transactional
    public void addRoute(String trainNumber, String startStation, String arrivalStation, LocalDate dateOfDispatch, LocalDate dateOfArrival
            , LocalTime timeOfDispatch, LocalTime timeOfArrival, LocalTime travelTime, int numberOfCompartmentSeats
            ,int numberOfSuiteSeats, BigDecimal priseOfCompartmentTicket, BigDecimal priseOfSuiteTicket) {
        Train train = trainService.findTrainByTrainNumber(trainNumber);
        Station stStation = stationService.findStationByStationName(startStation);
        Station endStation = stationService.findStationByStationName(arrivalStation);
        LocalDateTime departureTime = LocalDateTime.of(dateOfDispatch,timeOfDispatch);
        LocalDateTime arrivalTime = LocalDateTime.of(dateOfArrival,timeOfArrival);
        Route newRoute = new Route(stStation,departureTime,travelTime,endStation,arrivalTime,priseOfCompartmentTicket,priseOfSuiteTicket,train);
        routeRepository.save(newRoute);
    }*/

    @Override
    @Transactional
    public void addRoute(Map<String,String> allParam){
        String trainNumber = allParam.get("trainNumber");
        String startStation = allParam.get("startStation");
        String arrivalStation = allParam.get("arrivalStation");
        LocalDate dateOfDispatch = LocalDate.parse(allParam.get("dateOfDispatch"));
        LocalTime timeOfDispatch = LocalTime.parse(allParam.get("timeOfDispatch"));
        LocalDate dateOfArrival = LocalDate.parse(allParam.get("dateOfArrival"));
        LocalTime timeOfArrival = LocalTime.parse(allParam.get("timeOfArrival"));
        LocalTime travelTime = LocalTime.parse(allParam.get("travelTime"));
        LocalDateTime departureTime = LocalDateTime.of(dateOfDispatch,timeOfDispatch);
        LocalDateTime arrivalTime = LocalDateTime.of(dateOfArrival,timeOfArrival);
        int numberOfCompartmentFreeSeats = Integer.parseInt(allParam.get("numberOfCompartmentFreeSeats"));
        BigDecimal priseOfCompartmentTicket = BigDecimal.valueOf(Double.parseDouble(allParam.get("priseOfCompartmentTicket")));
        int numberOfSuiteFreeSeats = Integer.parseInt(allParam.get("numberOfSuiteFreeSeats"));
        BigDecimal priseOfSuiteTicket = BigDecimal.valueOf(Double.parseDouble(allParam.get("priseOfSuiteTicket")));
        Train train = trainService.findTrainByTrainNumber(trainNumber);
        if(train.getNumberOfCompartmentSeats() < numberOfCompartmentFreeSeats || train.getNumberOfSuiteSeats() < numberOfSuiteFreeSeats){
            throw new IllegalArgumentException("ліміт місць");
        }
        Station stStation = stationService.findStationByStationName(startStation);
        Station endStation = stationService.findStationByStationName(arrivalStation);
        Route route = new Route(stStation,departureTime,travelTime,endStation,arrivalTime,numberOfCompartmentFreeSeats,numberOfSuiteFreeSeats,priseOfCompartmentTicket,priseOfSuiteTicket,train);

        routeRepository.save(route);
    }

    @Override
    @Transactional
    public void updateRoute(Map<String,String> allParam) {
        Long id = Long.parseLong(allParam.get("id"));
        String trainNumber = allParam.get("trainNumber");
        String startStation = allParam.get("startStation");
        String arrivalStation = allParam.get("arrivalStation");
        LocalDate dateOfDispatch = LocalDate.parse(allParam.get("dateOfDispatch"));
        LocalTime timeOfDispatch = LocalTime.parse(allParam.get("timeOfDispatch"));
        LocalDate dateOfArrival = LocalDate.parse(allParam.get("dateOfArrival"));
        LocalTime timeOfArrival = LocalTime.parse(allParam.get("timeOfArrival"));
        LocalTime travelTime = LocalTime.parse(allParam.get("travelTime"));
        LocalDateTime departureTime = LocalDateTime.of(dateOfDispatch,timeOfDispatch);
        LocalDateTime arrivalTime = LocalDateTime.of(dateOfArrival,timeOfArrival);
        int numberOfCompartmentFreeSeats = Integer.parseInt(allParam.get("numberOfCompartmentFreeSeats"));
        BigDecimal priseOfCompartmentTicket = BigDecimal.valueOf(Double.parseDouble(allParam.get("priseOfCompartmentTicket")));
        int numberOfSuiteFreeSeats = Integer.parseInt(allParam.get("numberOfSuiteFreeSeats"));
        BigDecimal priseOfSuiteTicket = BigDecimal.valueOf(Double.parseDouble(allParam.get("priseOfSuiteTicket")));

        Route updateRoute = findById(id);
        Train train = trainService.findTrainByTrainNumber(trainNumber);
        Station stStation = stationService.findStationByStationName(startStation);
        Station endStation = stationService.findStationByStationName(arrivalStation);
        updateRoute.setArrivalStation(endStation);
        updateRoute.setStartStation(stStation);
        updateRoute.setTrain(train);
        updateRoute.setArrivalTime(arrivalTime);
        updateRoute.setDepartureTime(departureTime);
        updateRoute.setTravelTime(travelTime);
        updateRoute.setNumberOfCompartmentFreeSeats(numberOfCompartmentFreeSeats);
        updateRoute.setNumberOfSuiteFreeSeats(numberOfSuiteFreeSeats);
        updateRoute.setPriseOfCompartmentTicket(priseOfCompartmentTicket);
        updateRoute.setPriseOfSuiteTicket(priseOfSuiteTicket);

        routeRepository.save(updateRoute);
    }

    @Override
    @Transactional
    public void deleteRoute(Long Id) {
        routeRepository.deleteById(Id);
    }

    @Override
    @Transactional(readOnly = true)
    public Route findById(Long Id) {
        return routeRepository.findRouteById(Id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Route> getAllWayBetweenCitiesWithTime(String senderCity, String cityOfArrival, String selectedDatesString, String selectedTimeString, Pageable pageable, int pageNumber) {
        LocalDate selectedDates = LocalDate.parse(selectedDatesString);
        LocalTime selectedTime = LocalTime.parse(selectedTimeString);
        LocalDateTime selectedLocalDateTime = LocalDateTime.of(selectedDates,selectedTime);
        LocalDateTime finalLocalDateTime = selectedLocalDateTime.plusDays(7);
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize());
        return routeRepository.findAllByStartStation_City_CityNameAndArrivalStation_City_CityNameAndDepartureTimeBetween(
                senderCity, cityOfArrival, selectedLocalDateTime, finalLocalDateTime, changePageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Route> getAll(Pageable pageable, int pageNumber) {
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize());
        return routeRepository.findAll(changePageable);
    }

    @Override
    @Transactional
    public void reduceNumberOfCompartmentFreeSeats(Long routeId, int countOfPurchasedTickets) {
        routeRepository.reduceNumberOfCompartmentFreeSeats(routeId, countOfPurchasedTickets);
    }

    @Override
    @Transactional
    public void reduceNumberOfSuiteFreeSeats(Long routeId, int countOfPurchasedTickets) {
        routeRepository.reduceNumberOfSuiteFreeSeats(routeId, countOfPurchasedTickets);
    }
}
