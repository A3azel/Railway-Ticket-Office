package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.customExceptions.trainExceptions.TrainAlreadyExist;
import com.example.springbootpetproject.customExceptions.trainExceptions.TrainNotFound;
import com.example.springbootpetproject.dto.TrainDTO;
import com.example.springbootpetproject.entity.Train;
import com.example.springbootpetproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainServiceInterface {

    Page<TrainDTO> getAllTrain(Pageable pageable, int pageNumber, String direction, String sort);

    Train findTrainByTrainNumber(String trainNumber) throws TrainNotFound;

    void deleteTrainByID(Long id);

    void addTrain(Train train) throws TrainAlreadyExist;

    void updateTrain(Train train, Long id) throws TrainAlreadyExist;

    Train findTrainByID(Long id);

    boolean existTrainByTrainNumber(String trainNumber);

    void reduceTheNumberOfCompartmentSeats(String trainNumber, int countOfPurchasedTickets);

    void reduceTheNumberOfSuiteSeats(String trainNumber, int countOfPurchasedTickets);

    TrainDTO convertTrainToTrainDTO(Train train);

    void setTrainRelevant(Long id);
}
