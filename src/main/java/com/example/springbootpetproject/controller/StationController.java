package com.example.springbootpetproject.controller;


import com.example.springbootpetproject.entity.Station;
import com.example.springbootpetproject.service.serviceImplementation.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/station")
public class StationController {
    public final StationService stationService;

    @Autowired
    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Station>> getAll(){
        List<Station> list =stationService.getAllStationInCity("Київ");
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
