package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.entity.Train;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainServiceInterface {

    void addTrain(Train train);

    void updateTrain(Train train);

    Train getTrainByName(String trainName);

    List<Train> getAllWayBetweenCities(String senderCity, String cityOfArrival);

    List<Train> getAllWayBetweenCitiesInTime(String senderCity, String cityOfArrival, LocalDateTime departureTime);

    List<Train> getAllWayBetweenStationAndCity(String startStation,String cityOfArrival);

    List<Train> getAllWayBetweenStations(String startStation, String ArrivalStation);


}
