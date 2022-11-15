package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.service.serviceImplementation.StationServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/station")
public class StationController {
    public final StationServiceI stationServiceI;

    @Autowired
    public StationController(StationServiceI stationServiceI) {
        this.stationServiceI = stationServiceI;
    }

}
