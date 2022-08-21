package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.entity.Train;
import com.example.springbootpetproject.service.serviceImplementation.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/train")
public class TrainController {

    public final TrainService trainService;

    @Autowired
    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Train>> getAllTrain(){
        return new ResponseEntity<>(trainService.getAllWayBetweenCities("Київ","Львів"), HttpStatus.OK);
    }

    @GetMapping("/station")
    public ResponseEntity<List<Train>> getAllTrainByStation(){
        return new ResponseEntity<>(trainService.getAllWayBetweenStations("Київ Центр","Львів Центр"), HttpStatus.OK);
    }

    @GetMapping("/allTrain")
    public ResponseEntity<List<Train>> getAll(){
        return new ResponseEntity<>(trainService.getAllTrain(), HttpStatus.OK);
    }
}
