package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.entity.Train;
import com.example.springbootpetproject.service.serviceImplementation.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.example.springbootpetproject.controller.Paths.All_TRAINS_BETWEEN_CITIES_FILE;

@Controller
@RequestMapping("/train")
public class TrainController {

    public final TrainService trainService;

    @Autowired
    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @GetMapping("/between")
    public String getAllTrainBetweenCity(HttpServletRequest request, Model model){
        String st = request.getParameter("startCity");
        String en = request.getParameter("endCity");
        List<Train> trainList = trainService.getAllWayBetweenCities(st,en);
        model.addAttribute("trainList",trainList);
        return All_TRAINS_BETWEEN_CITIES_FILE;
    }

    @GetMapping("/info")
    public String getInfoAboutChangedTrain(HttpServletRequest request, Model model){
        Train train = trainService.getTrainByName(request.getParameter("infoAboutTicket"));
        model.addAttribute("selectedTrain",train);
        return "trainInfo";
    }


    // testing methods
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
