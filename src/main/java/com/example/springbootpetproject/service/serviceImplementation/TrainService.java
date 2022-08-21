package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.entity.Train;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.springbootpetproject.repository.TrainRepository;
import com.example.springbootpetproject.service.serviceInterfaces.TrainServiceInterface;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TrainService implements TrainServiceInterface {

    public final TrainRepository trainRepository;

    @Autowired
    public TrainService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    @Transactional
    public void addTrain(Train train) {
        trainRepository.save(train);
    }

    @Override
    @Transactional
    public void updateTrain(Train train) {
        trainRepository.save(train);

    }

    public List<Train> getAllTrain(){
        return trainRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Train> getAllWayBetweenCities(String senderCity, String cityOfArrival) {
        return trainRepository.getAllByStartStation_City_CityNameAndArrivalStation_City_CityName(senderCity, cityOfArrival);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Train> getAllWayBetweenCitiesInTime(String senderCity, String cityOfArrival, LocalDateTime departureTime) {
        return trainRepository.getByStartStation_City_CityNameAndArrivalStation_City_CityNameAndDepartureTime(senderCity, cityOfArrival, departureTime);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Train> getAllWayBetweenStationAndCity(String startStation, String cityOfArrival) {
        return trainRepository.getAllByStartStation_StationNameAndArrivalStation_City_CityName(startStation, cityOfArrival);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Train> getAllWayBetweenStations(String startStation, String ArrivalStation) {
        return trainRepository.getAllByStartStation_StationNameAndArrivalStation_StationName(startStation, ArrivalStation);
    }
}
