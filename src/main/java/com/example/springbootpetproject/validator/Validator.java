package com.example.springbootpetproject.validator;

import com.example.springbootpetproject.customExceptions.trainExceptions.TrainNotFound;
import com.example.springbootpetproject.dto.RouteDTO;
import com.example.springbootpetproject.entity.Train;
import com.example.springbootpetproject.service.serviceImplementation.CityServiceI;
import com.example.springbootpetproject.service.serviceImplementation.StationServiceI;
import com.example.springbootpetproject.service.serviceImplementation.TrainServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class Validator {
    private static TrainServiceI trainServiceI;
    private static StationServiceI stationServiceI;
    private static CityServiceI cityServiceI;

    private static final String PHONE_REGEX = "\\(?\\+?[0-9]{1,3}\\)? ?-?[0-9]{1,3} ?-?[0-9]{3,5} ?-?[0-9]{4}( ?-?[0-9]{3})? ?(\\w{1,10}\s?\\d{1,6})?";

    @Autowired
    public Validator(TrainServiceI trainServiceI, StationServiceI stationServiceI, CityServiceI cityServiceI) {
        Validator.trainServiceI = trainServiceI;
        Validator.stationServiceI = stationServiceI;
        Validator.cityServiceI = cityServiceI;
    }

    public static boolean isTheSamePassword(String password, String submitPassword){
        return password.equals(submitPassword);
    }

    public static boolean isPhoneValid(String phoneNumber){
        return Pattern.matches(PHONE_REGEX,phoneNumber);
    }

    public static boolean isPasswordLengthValid(String password){
        return password.length()>=8 && password.length()<=64;
    }

    public static Map<String,String> routeValidator(RouteDTO routeDTO){
        Map<String,String> errorsMap = new HashMap<>();
        Train selectedTrain = null;
        try {
            selectedTrain = trainServiceI.findTrainByTrainNumber(routeDTO.getTrainNumber());
            if(selectedTrain.getNumberOfCompartmentSeats()< routeDTO.getNumberOfCompartmentFreeSeats()){
                errorsMap.put("numberOfCompartmentSeatsProblems",String.format("The number of free seats in the compartment must be less than the number of all seats in the compartment (%s)",selectedTrain.getNumberOfCompartmentSeats()));
            }
            if(selectedTrain.getNumberOfSuiteSeats() < routeDTO.getNumberOfSuiteFreeSeats()){
                errorsMap.put("numberOfSuiteSeatsProblems",String.format("The number of free seats in the suite must be less than the number of all seats in the suite (%s)",selectedTrain.getNumberOfSuiteSeats()));
            }
        } catch (TrainNotFound e) {
            errorsMap.put("trainNotFound", "Train with the specified name was not found");
        }
        if(!cityServiceI.cityIsExist(routeDTO.getStartCityName())){
            errorsMap.put("firstCityNotFound", "City with the specified name was not found");
        }
        if(!cityServiceI.cityIsExist(routeDTO.getStartCityName())){
            errorsMap.put("secondCityNotFound", "City with the specified name was not found");
        }
        if(!stationServiceI.existStationByStationNameAndCity(routeDTO.getStartStationName(),routeDTO.getStartCityName())){
            errorsMap.put("firstStation","Station with the specified name was not found");
        }
        if(!routeDTO.getDepartureTime().isAfter(LocalDateTime.now())){
            errorsMap.put("firstDate","Selected date has already passed");
        }
        if(!stationServiceI.existStationByStationNameAndCity(routeDTO.getArrivalStationName(),routeDTO.getArrivalCityName())){
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
