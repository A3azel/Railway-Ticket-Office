package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.entity.Train;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.springbootpetproject.repository.TrainRepository;
import com.example.springbootpetproject.service.serviceInterfaces.TrainServiceInterface;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class TrainService implements TrainServiceInterface {
    private final TrainRepository trainRepository;
    private final StationService stationService;

    @Autowired
    public TrainService(TrainRepository trainRepository, StationService stationService) {
        this.trainRepository = trainRepository;
        this.stationService = stationService;
    }

    @Override
    @Transactional
    public void addTrain(Train train) {
        trainRepository.save(train);
    }

    @Override
    @Transactional
    public void updateTrain(String id, String trainNumber, String startStation, String departureData,String departureTime, String travelTime,
                            String arrivalStation,String arrivalData, String arrivalTime, String numberOfFreeSeats, String priseOfTicket) {
        Train updateTrain = findTrainByID(Long.parseLong(id));
        updateTrain.setTrainNumber(trainNumber);
        updateTrain.setStartStation(stationService.findStationByStationName(startStation));
        LocalDate selectedDates = LocalDate.parse(departureData);
        LocalTime selectedTime = LocalTime.parse(departureTime);
        LocalDateTime departureLocalDateTime = LocalDateTime.of(selectedDates,selectedTime);
        updateTrain.setDepartureTime(departureLocalDateTime);
        //updateTrain.setDepartureTime(LocalDateTime.parse(departureTime));
        updateTrain.setTravelTime(LocalTime.parse(travelTime));
        updateTrain.setArrivalStation(stationService.findStationByStationName(arrivalStation));
        LocalDate selectedArrivalDates = LocalDate.parse(arrivalData);
        LocalTime selectedArrivalTime = LocalTime.parse(arrivalTime);
        LocalDateTime arrivalLocalDateTime = LocalDateTime.of(selectedArrivalDates,selectedArrivalTime);
        updateTrain.setArrivalTime(arrivalLocalDateTime);
        //updateTrain.setArrivalTime(LocalDateTime.parse(arrivalTime));
        updateTrain.setNumberOfFreeSeats(Integer.parseInt(numberOfFreeSeats));
        double priseOfTicketDouble = Double.parseDouble(priseOfTicket);
        updateTrain.setPriseOfTicket(BigDecimal.valueOf(priseOfTicketDouble));
        if(priseOfTicketDouble<0){
            throw new IllegalArgumentException("Ціна не може бути мньше нуля");
        }
        trainRepository.save(updateTrain);
    }


    @Override
    @Transactional
    public Train getTrainByName(String trainName) {
        return trainRepository.getTrainByTrainNumber(trainName);
    }

    @Transactional
    public Train findTrainByID(Long id){
        return trainRepository.findTrainById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Train> getAllTrain(Pageable pageable, int pageNumber){
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize());
        return trainRepository.findAll(changePageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Train> getAllWayBetweenCities(String senderCity, String cityOfArrival, Pageable pageable, int pageNumber) {
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize());
        return trainRepository.getAllByStartStation_City_CityNameAndArrivalStation_City_CityName(senderCity, cityOfArrival, changePageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Train> getAllWayBetweenCitiesWithTime(
            String senderCity, String cityOfArrival, LocalDateTime selectedDates, LocalDateTime finalDates, Pageable pageable, int pageNumber) {
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize());
        return trainRepository.findAllByStartStation_City_CityNameAndArrivalStation_City_CityNameAndDepartureTimeBetween(
                senderCity, cityOfArrival, selectedDates, finalDates, changePageable);
    }
    //
    @Override
    @Transactional(readOnly = true)
    public List<Train> getAllWayBetweenCities(String senderCity, String cityOfArrival) {
        return trainRepository.getAllByStartStation_City_CityNameAndArrivalStation_City_CityName(senderCity, cityOfArrival);
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
