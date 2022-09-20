package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.customExceptions.CityExceptions.InvalidNameOfCity;
import com.example.springbootpetproject.dto.RouteDTO;
import com.example.springbootpetproject.dto.UserCommentsDTO;
import com.example.springbootpetproject.entity.Route;
import com.example.springbootpetproject.entity.Train;
import com.example.springbootpetproject.entity.UserComments;

import com.example.springbootpetproject.service.serviceImplementation.RouteService;
import com.example.springbootpetproject.service.serviceImplementation.TrainService;
import com.example.springbootpetproject.service.serviceImplementation.UserCommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.example.springbootpetproject.controller.Paths.All_TRAINS_BETWEEN_CITIES_FILE;

@Controller
@RequestMapping("/train")
public class TrainController {

    public final TrainService trainService;
    public final UserCommentsService userCommentsService;
    private final RouteService routeService;

    @Autowired
    public TrainController(TrainService trainService, UserCommentsService userCommentsService, RouteService routeService) {
        this.trainService = trainService;
        this.userCommentsService = userCommentsService;
        this.routeService = routeService;
    }

    @GetMapping("/between/page/{pageNumber}")
    public String getAllTrainBetweenCity(Model model
            , @PageableDefault(size = 10) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam("cityOfDeparture") String cityOfDeparture,@RequestParam("cityOfArrival") String cityOfArrival
            , @RequestParam("selectedDates") String selectedDatesString,@RequestParam("selectedTime") String selectedTimeString
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){

        Page<RouteDTO> routeDTOPage = null;
        try {
            routeDTOPage = routeService.getAllWayBetweenCitiesWithTime(cityOfDeparture,cityOfArrival,selectedDatesString,selectedTimeString,pageable,pageNumber,direction,sort);
        } catch (InvalidNameOfCity e) {
            return "TestPage";
        }
        List<RouteDTO> routeDTOList = routeDTOPage.getContent();

        model.addAttribute("selectedDates",selectedDatesString);
        model.addAttribute("selectedTime",selectedTimeString);
        model.addAttribute("cityOfDeparture",cityOfDeparture);
        model.addAttribute("cityOfArrival",cityOfArrival);

        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",routeDTOPage);
        model.addAttribute("routeDTOList",routeDTOList);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");

        return All_TRAINS_BETWEEN_CITIES_FILE;
    }

    @GetMapping("/info")
    public String getInfoAboutChangedTrain(@RequestParam("id") String id, Model model){
        Route route = routeService.findById(Long.parseLong(id));
        List<UserCommentsDTO> userCommentsList = userCommentsService.findByTrainNumber(route.getTrain().getTrainNumber());
        model.addAttribute("userCommentsList",userCommentsList);
        model.addAttribute("selectedRoute",route);
        return "trainInfo";
    }

}


