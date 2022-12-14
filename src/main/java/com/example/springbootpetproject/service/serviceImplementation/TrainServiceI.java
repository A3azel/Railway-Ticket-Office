package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.customExceptions.trainExceptions.TrainAlreadyExist;
import com.example.springbootpetproject.customExceptions.trainExceptions.TrainNotFound;
import com.example.springbootpetproject.dto.TrainDTO;
import com.example.springbootpetproject.entity.Train;
import com.example.springbootpetproject.facade.TrainFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.springbootpetproject.repository.TrainRepository;

@Service
public class TrainServiceI implements com.example.springbootpetproject.service.serviceInterfaces.TrainService {
    private final TrainRepository trainRepository;
    public final TrainFacade trainFacade;

    @Autowired
    public TrainServiceI(TrainRepository trainRepository, TrainFacade trainFacade) {
        this.trainRepository = trainRepository;
        this.trainFacade = trainFacade;
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
        Page<TrainDTO> trainDTOPage = trainPage.map(trainFacade::convertTrainToTrainDTO);
        return trainDTOPage;
    }

    @Override
    @Transactional
    public Train findTrainByTrainNumber(String trainNumber) throws TrainNotFound {
        return trainRepository.findTrainByTrainNumber(trainNumber)
                .orElseThrow(()-> new TrainNotFound("Train with the specified name not found"));
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
    public void updateTrain(TrainDTO trainDTO, Long id) throws TrainAlreadyExist {
        Train selectedTrain = findTrainByID(id);
        if(existTrainByTrainNumber(trainDTO.getTrainNumber())&& !selectedTrain.getTrainNumber().equals(trainDTO.getTrainNumber())){
            throw new TrainAlreadyExist("Train with the specified name already exist");
        }
        selectedTrain.setTrainNumber(trainDTO.getTrainNumber());
        selectedTrain.setNumberOfSuiteSeats(trainDTO.getNumberOfSuiteSeats());
        selectedTrain.setNumberOfCompartmentSeats(trainDTO.getNumberOfCompartmentSeats());
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

}
