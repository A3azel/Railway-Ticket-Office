package com.example.springbootpetproject.validator;

import com.example.springbootpetproject.customExceptions.trainExceptions.TrainNotFound;
import com.example.springbootpetproject.dto.RouteDTO;
import com.example.springbootpetproject.entity.Train;
import com.example.springbootpetproject.service.serviceImplementation.CityService;
import com.example.springbootpetproject.service.serviceImplementation.StationService;
import com.example.springbootpetproject.service.serviceImplementation.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class Validator {
    private static TrainService trainService;
    private static StationService stationService;
    private static CityService cityService;

    @Autowired
    public Validator(TrainService trainService, StationService stationService, CityService cityService) {
        Validator.trainService = trainService;
        Validator.stationService = stationService;
        Validator.cityService = cityService;
    }

    public static boolean isTheSamePassword(String password, String submitPassword){
        return password.equals(submitPassword);
    }

    public static boolean isPasswordLengthValid(String password){
        return password.length()>=8 && password.length()<=64;
    }

    public static Map<String,String> routeValidator(RouteDTO routeDTO){
        Map<String,String> errorsMap = new HashMap<>();
        Train selectedTrain = null;
        try {
            selectedTrain = trainService.findTrainByTrainNumber(routeDTO.getTrainNumber());
            if(selectedTrain.getNumberOfCompartmentSeats()< routeDTO.getNumberOfCompartmentFreeSeats()){
                errorsMap.put("numberOfCompartmentSeatsProblems",String.format("The number of free seats in the compartment must be less than the number of all seats in the compartment (%s)",selectedTrain.getNumberOfCompartmentSeats()));
            }
            if(selectedTrain.getNumberOfSuiteSeats() < routeDTO.getNumberOfSuiteFreeSeats()){
                errorsMap.put("numberOfSuiteSeatsProblems",String.format("The number of free seats in the suite must be less than the number of all seats in the suite (%s)",selectedTrain.getNumberOfSuiteSeats()));
            }
        } catch (TrainNotFound e) {
            errorsMap.put("trainNotFound", "Train with the specified name was not found");
        }
        if(!cityService.cityIsExist(routeDTO.getStartCityName())){
            errorsMap.put("firstCityNotFound", "City with the specified name was not found");
        }
        if(!cityService.cityIsExist(routeDTO.getStartCityName())){
            errorsMap.put("secondCityNotFound", "City with the specified name was not found");
        }
        if(!stationService.existStationByStationNameAndCity(routeDTO.getStartStationName(),routeDTO.getStartCityName())){
            errorsMap.put("firstStation","Station with the specified name was not found");
        }
        if(!routeDTO.getDepartureTime().isAfter(LocalDateTime.now())){
            errorsMap.put("firstDate","Selected date has already passed");
        }
        if(!stationService.existStationByStationNameAndCity(routeDTO.getArrivalStationName(),routeDTO.getArrivalCityName())){
            errorsMap.put("secondStation","Station with the specified name was not found");
        }
        if(!routeDTO.getArrivalTime().isAfter(LocalDateTime.now())){
            errorsMap.put("secondDate","Selected date has already passed");
        }
        if(!routeDTO.getArrivalTime().isAfter(routeDTO.getDepartureTime())){
            errorsMap.put("dateProblems","Selected date must be after the departure time");
        }
        return errorsMap;
    }

}
