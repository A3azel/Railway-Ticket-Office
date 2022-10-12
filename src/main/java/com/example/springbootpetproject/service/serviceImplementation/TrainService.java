package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.customExceptions.trainExceptions.TrainAlreadyExist;
import com.example.springbootpetproject.dto.TrainDTO;
import com.example.springbootpetproject.entity.Train;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.springbootpetproject.repository.TrainRepository;
import com.example.springbootpetproject.service.serviceInterfaces.TrainServiceInterface;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class TrainService implements TrainServiceInterface {
    private final TrainRepository trainRepository;

    @Autowired
    public TrainService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    @Transactional
    public Train findTrainByID(Long id){
        return trainRepository.findTrainById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existTrainByTrainNumber(String trainNumber) {
        return trainRepository.existsTrainByTrainNumber(trainNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrainDTO> getAllTrain(Pageable pageable, int pageNumber, String direction, String sort){
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()
                ,direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        Page<Train> trainPage = trainRepository.findAll(changePageable);
        Page<TrainDTO> trainDTOPage = trainPage.map(this::convertTrainToTrainDTO);
        return trainDTOPage;
    }

    @Override
    @Transactional
    public Train findTrainByTrainNumber(String trainNumber) {
        return trainRepository.findTrainByTrainNumber(trainNumber);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTrainByID(Long id){
        trainRepository.deleteTrainById(id);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void addTrain(Train train) throws TrainAlreadyExist {
        if(existTrainByTrainNumber(train.getTrainNumber())){
            throw new TrainAlreadyExist("Train with the specified name already exist");
        }
        train.setRelevant(true);
        trainRepository.save(train);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void updateTrain(Train train, Long id) throws TrainAlreadyExist {
        if(existTrainByTrainNumber(train.getTrainNumber())){
            throw new TrainAlreadyExist("Train with the specified name already exist");
        }
        Train selectedTrain = findTrainByID(id);
        selectedTrain.setTrainNumber(train.getTrainNumber());
        selectedTrain.setNumberOfSuiteSeats(train.getNumberOfSuiteSeats());
        selectedTrain.setNumberOfCompartmentSeats(train.getNumberOfCompartmentSeats());
        trainRepository.save(selectedTrain);
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

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void setTrainRelevant(Long id) {
        boolean isRelevant = findTrainByID(id).isRelevant();
        boolean newRelevant = !isRelevant;
        trainRepository.setTrainRelevant(newRelevant,id);
    }

    @Override
    public TrainDTO convertTrainToTrainDTO(Train train){
        TrainDTO trainDTO = new TrainDTO();
        trainDTO.setId(train.getId());
        trainDTO.setTrainNumber(train.getTrainNumber());
        trainDTO.setNumberOfCompartmentSeats(train.getNumberOfCompartmentSeats());
        trainDTO.setNumberOfSuiteSeats(train.getNumberOfSuiteSeats());
        trainDTO.setRelevant(train.isRelevant());
        return trainDTO;
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
