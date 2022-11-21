package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.customExceptions.routeExceptions.RouteNotFound;
import com.example.springbootpetproject.customExceptions.trainExceptions.TrainNotFound;
import com.example.springbootpetproject.dto.RouteDTO;
import com.example.springbootpetproject.entity.Route;
import com.example.springbootpetproject.facade.RouteFacade;
import com.example.springbootpetproject.repository.RouteRepository;
import com.example.springbootpetproject.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@Service
@Slf4j
public class RouteServiceI implements com.example.springbootpetproject.service.serviceInterfaces.RouteService {
    private final RouteRepository routeRepository;
    private final TrainServiceI trainServiceI;
    private final StationServiceI stationServiceI;
    private final RouteFacade routeFacade;

    @Autowired
    public RouteServiceI(RouteRepository routeRepository, TrainServiceI trainServiceI, StationServiceI stationServiceI, RouteFacade routeFacade) {
        this.routeRepository = routeRepository;
        this.trainServiceI = trainServiceI;
        this.stationServiceI = stationServiceI;
        this.routeFacade = routeFacade;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String,String> addRoute(RouteDTO routeDTO) {
        Map<String,String> errorsMap = Validator.routeValidator(routeDTO);
        if(!errorsMap.isEmpty()){
            return errorsMap;
        }
        Route route = new Route();
        Duration duration = Duration.between(routeDTO.getArrivalTime(),routeDTO.getDepartureTime());
        LocalTime travelTime = LocalTime.of(duration.toHoursPart(),duration.toMinutesPart());

        route.setStartStation(stationServiceI.findStationByStationName(routeDTO.getStartStationName()));
        route.setDepartureTime(routeDTO.getDepartureTime());
        route.setTravelTime(travelTime);
        route.setArrivalStation(stationServiceI.findStationByStationName(routeDTO.getArrivalStationName()));
        route.setArrivalTime(routeDTO.getArrivalTime());
        route.setNumberOfCompartmentFreeSeats((routeDTO.getNumberOfCompartmentFreeSeats()));
        route.setNumberOfSuiteFreeSeats(routeDTO.getNumberOfSuiteFreeSeats());
        route.setPriseOfCompartmentTicket(routeDTO.getPriseOfCompartmentTicket());
        route.setPriseOfSuiteTicket(routeDTO.getPriseOfSuiteTicket());
        try {
            route.setTrain(trainServiceI.findTrainByTrainNumber(routeDTO.getTrainNumber()));
        } catch (TrainNotFound e) {
            e.printStackTrace();
        }
        routeRepository.save(route);
        return errorsMap;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String,String> updateRoute(RouteDTO routeDTO, Long id) {
        Map<String,String> errorsMap = Validator.routeValidator(routeDTO);
        Route route = findRouteById(id);
        Duration duration = Duration.between(routeDTO.getDepartureTime(),routeDTO.getArrivalTime());
        LocalTime travelTime = LocalTime.of(duration.toHoursPart(),duration.toMinutesPart());

        route.setStartStation(stationServiceI.findStationByStationName(routeDTO.getStartStationName()));
        route.setDepartureTime(routeDTO.getDepartureTime());
        route.setTravelTime(travelTime);
        route.setArrivalStation(stationServiceI.findStationByStationName(routeDTO.getArrivalStationName()));
        route.setArrivalTime(routeDTO.getArrivalTime());
        route.setNumberOfCompartmentFreeSeats((routeDTO.getNumberOfCompartmentFreeSeats()));
        route.setNumberOfSuiteFreeSeats(routeDTO.getNumberOfSuiteFreeSeats());
        route.setPriseOfCompartmentTicket(routeDTO.getPriseOfCompartmentTicket());
        route.setPriseOfSuiteTicket(routeDTO.getPriseOfSuiteTicket());
        try {
            route.setTrain(trainServiceI.findTrainByTrainNumber(routeDTO.getTrainNumber()));
        } catch (TrainNotFound e) {
            e.printStackTrace();
        }
        routeRepository.save(route);
        return errorsMap;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRoute(Long Id) {
        routeRepository.deleteById(Id);
    }

    @Override
    @Transactional(readOnly = true)
    public Route findRouteById(Long Id)  {
        return routeRepository.findRouteById(Id);
    }

    @Override
    @Transactional(readOnly = true)
    public Route findById(Long Id) throws RouteNotFound {
        return routeRepository.findById(Id).orElseThrow(()->new RouteNotFound("Route with the specified ID was not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouteDTO> getAllWayBetweenCitiesWithTime(String senderCity, String cityOfArrival, String selectedDatesString, String selectedTimeString, Pageable pageable
            , int pageNumber, String direction, String sort) throws RouteNotFound {
        System.out.println(senderCity);
        System.out.println(cityOfArrival);

        LocalDate selectedDates = LocalDate.parse(selectedDatesString);
        LocalTime selectedTime = LocalTime.parse(selectedTimeString);
        LocalDateTime selectedLocalDateTime = LocalDateTime.of(selectedDates,selectedTime);
        LocalDateTime finalLocalDateTime = selectedLocalDateTime.plusDays(7);
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()
                ,direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        Page<Route> routePage = routeRepository.findAllByStartStation_RelevantIsTrueAndArrivalStation_RelevantIsTrueAndStartStation_City_CityNameAndArrivalStation_City_CityNameAndDepartureTimeBetween(
                senderCity, cityOfArrival, selectedLocalDateTime, finalLocalDateTime, changePageable);
        Page<RouteDTO> routeDTOPage = routePage.map(routeFacade::convertRouteToRouteDTO);
        if (routeDTOPage.getContent().size() == 0 || selectedLocalDateTime.isBefore(LocalDateTime.now())){
            throw new RouteNotFound("No routes were found for this request");
        }
        return routeDTOPage;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouteDTO> getAll(Pageable pageable, int pageNumber, String direction, String sort) {
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()
                ,direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        return routeRepository
                .findAll(changePageable)
                .map(routeFacade::convertRouteToRouteDTO);
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
