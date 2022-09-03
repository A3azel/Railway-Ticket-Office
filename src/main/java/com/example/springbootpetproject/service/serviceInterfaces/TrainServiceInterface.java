package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.entity.Train;
import com.example.springbootpetproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainServiceInterface {

    void addTrain(Train train);

    void updateTrain(String id, String trainNumber,String startStation, String departureData,String departureTime, String travelTime,
                     String arrivalStation,String arrivalData, String arrivalTime, String numberOfFreeSeats, String priseOfTicket);

    Train getTrainByName(String trainName);

    //List<Train> getAllTrain();

    Page<Train> getAllTrain(Pageable pageable, int pageNumber);

    Page<Train> getAllWayBetweenCitiesWithTime(String senderCity, String cityOfArrival, LocalDateTime selectedDates, LocalDateTime finalDates, Pageable pageable, int pageNumber);

    List<Train> getAllWayBetweenCities(String senderCity, String cityOfArrival);

    Page<Train> getAllWayBetweenCities(String senderCity, String cityOfArrival, Pageable pageable, int pageNumber);

    List<Train> getAllWayBetweenStationAndCity(String startStation,String cityOfArrival);

    List<Train> getAllWayBetweenStations(String startStation, String ArrivalStation);


}
