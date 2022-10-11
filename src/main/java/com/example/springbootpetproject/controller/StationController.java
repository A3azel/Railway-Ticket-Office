package com.example.springbootpetproject.controller;


import com.example.springbootpetproject.repository.RouteRepository;
import com.example.springbootpetproject.repository.StationRepository;
import com.example.springbootpetproject.service.serviceImplementation.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/station")
public class StationController {
    public final StationService stationService;

    @Autowired
    public StationController(StationService stationService) {
        this.stationService = stationService;
    }


}
