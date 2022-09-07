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


/*
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
    public void addTrain(String trainNumber, String startStation, String departureData,String departureTime, String travelTime,
                         String arrivalStation,String arrivalData, String arrivalTime, String numberOfFreeSeats, String priseOfTicket) {
        Train newTrain = new Train();
        newTrain.setTrainNumber(trainNumber);
        newTrain.setStartStation(stationService.findStationByStationName(startStation));
        LocalDateTime departureLocalDateTime = localDateTimeBuilder(departureData,departureTime);
        newTrain.setDepartureTime(departureLocalDateTime);
        newTrain.setTravelTime(LocalTime.parse(travelTime));
        newTrain.setArrivalStation(stationService.findStationByStationName(arrivalStation));
        LocalDateTime arrivalLocalDateTime =localDateTimeBuilder(arrivalData,arrivalTime);
        newTrain.setArrivalTime(arrivalLocalDateTime);
        newTrain.setNumberOfFreeSeats(Integer.parseInt(numberOfFreeSeats));
        double priseOfTicketDouble = Double.parseDouble(priseOfTicket);
        newTrain.setPriseOfTicket(BigDecimal.valueOf(priseOfTicketDouble));
        if(priseOfTicketDouble<0){
            throw new IllegalArgumentException("Ціна не може бути мньше нуля");
        }
        trainRepository.save(newTrain);
    }

    @Override
    @Transactional
    public void updateTrain(String id, String trainNumber, String startStation, String departureData,String departureTime, String travelTime,
                            String arrivalStation,String arrivalData, String arrivalTime, String numberOfFreeSeats, String priseOfTicket) {

        Train updateTrain = findTrainByID(Long.parseLong(id));
        updateTrain.setTrainNumber(trainNumber);
        updateTrain.setStartStation(stationService.findStationByStationName(startStation));
        LocalDateTime departureLocalDateTime = localDateTimeBuilder(departureData,departureTime);
        updateTrain.setDepartureTime(departureLocalDateTime);
        updateTrain.setTravelTime(LocalTime.parse(travelTime));
        updateTrain.setArrivalStation(stationService.findStationByStationName(arrivalStation));
        LocalDateTime arrivalLocalDateTime =localDateTimeBuilder(arrivalData,arrivalTime);
        updateTrain.setArrivalTime(arrivalLocalDateTime);
        updateTrain.setNumberOfFreeSeats(Integer.parseInt(numberOfFreeSeats));
        double priseOfTicketDouble = Double.parseDouble(priseOfTicket);
        updateTrain.setPriseOfTicket(BigDecimal.valueOf(priseOfTicketDouble));

        */
/*updateTrain.setTrainNumber(trainNumber);
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
        updateTrain.setPriseOfTicket(BigDecimal.valueOf(priseOfTicketDouble));*//*


        if(priseOfTicketDouble<0){
            throw new IllegalArgumentException("Ціна не може бути мньше нуля");
        }
        trainRepository.save(updateTrain);
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
            String senderCity, String cityOfArrival, String selectedDatesString, String selectedTimeString, Pageable pageable, int pageNumber) {
        LocalDate selectedDates = LocalDate.parse(selectedDatesString);
        LocalTime selectedTime = LocalTime.parse(selectedTimeString);
        LocalDateTime selectedLocalDateTime = LocalDateTime.of(selectedDates,selectedTime);
        LocalDateTime finalLocalDateTime = selectedLocalDateTime.plusDays(7);
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize());
        return trainRepository.findAllByStartStation_City_CityNameAndArrivalStation_City_CityNameAndDepartureTimeBetween(
                senderCity, cityOfArrival, selectedLocalDateTime, finalLocalDateTime, changePageable);
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

    @Override
    @Transactional
    public Train findTrainByTrainNumber(String trainNumber) {
        return trainRepository.findTrainByTrainNumber(trainNumber);
    }

    @Override
    @Transactional
    public void deleteTrainByID(Long id){
        trainRepository.deleteById(id);
    }

    public LocalDateTime localDateTimeBuilder(String localDate,String localTime){
        LocalDate selectedDates = LocalDate.parse(localDate);
        LocalTime selectedTime = LocalTime.parse(localTime);
        LocalDateTime localDateTime = LocalDateTime.of(selectedDates,selectedTime);
        return localDateTime;
    }

    public Train helpToBuildTrain(Train train){
        return null;
    }
}
*/


@Service
public class TrainService implements TrainServiceInterface {
    private final TrainRepository trainRepository;

    @Autowired
    public TrainService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    /*@Override
    @Transactional
    public void addTrain(String trainNumber, String startStation, String departureData,String departureTime, String travelTime,
                         String arrivalStation,String arrivalData, String arrivalTime, String numberOfFreeSeats, String priseOfTicket) {
        Train newTrain = new Train();
        newTrain.setTrainNumber(trainNumber);
        newTrain.setStartStation(stationService.findStationByStationName(startStation));
        LocalDateTime departureLocalDateTime = localDateTimeBuilder(departureData,departureTime);
        newTrain.setDepartureTime(departureLocalDateTime);
        newTrain.setTravelTime(LocalTime.parse(travelTime));
        newTrain.setArrivalStation(stationService.findStationByStationName(arrivalStation));
        LocalDateTime arrivalLocalDateTime =localDateTimeBuilder(arrivalData,arrivalTime);
        newTrain.setArrivalTime(arrivalLocalDateTime);
        newTrain.setNumberOfFreeSeats(Integer.parseInt(numberOfFreeSeats));
        double priseOfTicketDouble = Double.parseDouble(priseOfTicket);
        newTrain.setPriseOfTicket(BigDecimal.valueOf(priseOfTicketDouble));
        if(priseOfTicketDouble<0){
            throw new IllegalArgumentException("Ціна не може бути мньше нуля");
        }
        trainRepository.save(newTrain);
    }

    @Override
    @Transactional
    public void updateTrain(String id, String trainNumber, String startStation, String departureData,String departureTime, String travelTime,
                            String arrivalStation,String arrivalData, String arrivalTime, String numberOfFreeSeats, String priseOfTicket) {

        Train updateTrain = findTrainByID(Long.parseLong(id));
        updateTrain.setTrainNumber(trainNumber);
        updateTrain.setStartStation(stationService.findStationByStationName(startStation));
        LocalDateTime departureLocalDateTime = localDateTimeBuilder(departureData,departureTime);
        updateTrain.setDepartureTime(departureLocalDateTime);
        updateTrain.setTravelTime(LocalTime.parse(travelTime));
        updateTrain.setArrivalStation(stationService.findStationByStationName(arrivalStation));
        LocalDateTime arrivalLocalDateTime =localDateTimeBuilder(arrivalData,arrivalTime);
        updateTrain.setArrivalTime(arrivalLocalDateTime);
        updateTrain.setNumberOfFreeSeats(Integer.parseInt(numberOfFreeSeats));
        double priseOfTicketDouble = Double.parseDouble(priseOfTicket);
        updateTrain.setPriseOfTicket(BigDecimal.valueOf(priseOfTicketDouble));

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
    }*/

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
    @Transactional
    public Train findTrainByTrainNumber(String trainNumber) {
        return trainRepository.findTrainByTrainNumber(trainNumber);
    }

    @Override
    @Transactional
    public void deleteTrainByID(Long id){
        trainRepository.deleteById(id);
    }

    @Override
    public void addTrain(Train train) {
        trainRepository.save(train);
    }

    @Override
    public void updateTrain(Train train) {
        trainRepository.save(train);
    }

    @Override
    @Transactional
    public void reduceTheNumberOfCompartmentSeats(String trainNumber, int countOfPurchasedTickets) {
        trainRepository.reduceTheNumberOfCompartmentSeats(trainNumber, countOfPurchasedTickets);
    }

    @Override
    @Transactional
    public void reduceTheNumberOfSuiteSeats(String trainNumber, int countOfPurchasedTickets) {
        trainRepository.reduceTheNumberOfSuiteSeats(trainNumber, countOfPurchasedTickets);
    }

    public LocalDateTime localDateTimeBuilder(String localDate,String localTime){
        LocalDate selectedDates = LocalDate.parse(localDate);
        LocalTime selectedTime = LocalTime.parse(localTime);
        LocalDateTime localDateTime = LocalDateTime.of(selectedDates,selectedTime);
        return localDateTime;
    }

    public Train helpToBuildTrain(Train train){
        return null;
    }
}
