package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.customExceptions.cityExceptions.InvalidNameOfCity;
import com.example.springbootpetproject.customExceptions.routeExceptions.DataCompareError;
import com.example.springbootpetproject.customExceptions.routeExceptions.ProblemWithSeatsCount;
import com.example.springbootpetproject.customExceptions.routeExceptions.RouteNotFound;
import com.example.springbootpetproject.customExceptions.stationExceptions.StationNotFound;
import com.example.springbootpetproject.customExceptions.trainExceptions.TrainNotFound;
import com.example.springbootpetproject.dto.RouteDTO;
import com.example.springbootpetproject.entity.Route;
import com.example.springbootpetproject.entity.Station;
import com.example.springbootpetproject.entity.Train;
import com.example.springbootpetproject.repository.RouteRepository;
import com.example.springbootpetproject.service.serviceInterfaces.RouteServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
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
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String,String> addRoute(RouteDTO routeDTO) {
        Map<String,String> errorsMap = new HashMap<>();
        if(!stationService.existStationByStationNameAndCity(routeDTO.getStartStationName(),routeDTO.getStartCityName())){
            errorsMap.put("First Station","Station with the specified name was not found");
            //throw new StationNotFound("Station with the specified name was not found");
        }
        if(!routeDTO.getDepartureTime().isAfter(LocalDateTime.now())){
            errorsMap.put("First Date","Selected date has already passed");
            //throw new DataCompareError("Selected date has already passed");
        }
        if(!stationService.existStationByStationNameAndCity(routeDTO.getArrivalStationName(),routeDTO.getArrivalCityName())){
            errorsMap.put("Second Station","Station with the specified name was not found");
            //throw new StationNotFound("Station with the specified name was not found");
        }
        if(!routeDTO.getArrivalTime().isAfter(LocalDateTime.now())){
            errorsMap.put("Second Date","Selected date has already passed");
            //throw new DataCompareError("Selected date has already passed");
        }
        if(!routeDTO.getArrivalTime().isAfter(routeDTO.getDepartureTime())){
            errorsMap.put("Date problems","Selected date must be after the departure time");
            //throw new DataCompareError("Selected date must be after the departure time");
        }
        if(routeDTO.getNumberOfCompartmentSeats()< routeDTO.getNumberOfCompartmentFreeSeats()){
            errorsMap.put("Number of seats problems","Selected date must be after the departure time");
            //throw new ProblemWithSeatsCount("");
        }
        if(!errorsMap.isEmpty()){
            return errorsMap;
        }

        Route route = new Route();
        route.setStartStation(stationService.findStationByStationName(routeDTO.getStartStationName()));
        route.setDepartureTime(routeDTO.getDepartureTime());
        route.setTravelTime(routeDTO.getTravelTime());
        route.setArrivalStation(stationService.findStationByStationName(routeDTO.getArrivalStationName()));
        route.setArrivalTime(routeDTO.getArrivalTime());
        route.setNumberOfCompartmentFreeSeats((routeDTO.getNumberOfCompartmentFreeSeats()));
        route.setNumberOfSuiteFreeSeats(routeDTO.getNumberOfSuiteFreeSeats());
        route.setPriseOfCompartmentTicket(routeDTO.getPriseOfCompartmentTicket());
        route.setPriseOfSuiteTicket(routeDTO.getPriseOfSuiteTicket());


/*        String trainNumber = allParam.get("trainNumber");
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
*/
        routeRepository.save(new Route());
        return errorsMap;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
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

        Route updateRoute = findRouteById(id);
        Train train = null;
        try {
            train = trainService.findTrainByTrainNumber(trainNumber);
        } catch (TrainNotFound e) {
            e.printStackTrace();
        }
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
        LocalDate selectedDates = LocalDate.parse(selectedDatesString);
        LocalTime selectedTime = LocalTime.parse(selectedTimeString);
        LocalDateTime selectedLocalDateTime = LocalDateTime.of(selectedDates,selectedTime);
        LocalDateTime finalLocalDateTime = selectedLocalDateTime.plusDays(7);
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()
                ,direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        Page<Route> routePage = routeRepository.findAllByStartStation_RelevantIsTrueAndArrivalStation_RelevantIsTrueAndStartStation_City_CityNameAndArrivalStation_City_CityNameAndDepartureTimeBetween(
                senderCity, cityOfArrival, selectedLocalDateTime, finalLocalDateTime, changePageable);
        Page<RouteDTO> routeDTOPage = routePage.map(this::convertRouteToRouteDTO);
        if (routeDTOPage.getContent().size() == 0){
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
                .map(this::convertRouteToRouteDTO);
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

    @Override
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
        return routeDTO;
    }

}
