package com.example.springbootpetproject.facade;

import com.example.springbootpetproject.dto.TrainDTO;
import com.example.springbootpetproject.entity.Train;
import org.springframework.stereotype.Component;

import java.text.Format;
import java.text.SimpleDateFormat;

@Component
public class TrainFacade {

    public TrainDTO convertTrainToTrainDTO(Train train){
        TrainDTO trainDTO = new TrainDTO();
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        trainDTO.setId(train.getId());
        trainDTO.setCreated(formatter.format(train.getCreated()));
        trainDTO.setUpdated(formatter.format(train.getUpdated()));
        trainDTO.setCreatedBy(train.getCreatedBy());
        trainDTO.setLastModifiedBy(train.getLastModifiedBy());
        trainDTO.setTrainNumber(train.getTrainNumber());
        trainDTO.setNumberOfCompartmentSeats(train.getNumberOfCompartmentSeats());
        trainDTO.setNumberOfSuiteSeats(train.getNumberOfSuiteSeats());
        trainDTO.setRelevant(train.isRelevant());
        return trainDTO;
    }
}
