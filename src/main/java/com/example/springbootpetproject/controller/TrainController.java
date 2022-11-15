package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.customExceptions.routeExceptions.RouteNotFound;
import com.example.springbootpetproject.dto.RouteDTO;
import com.example.springbootpetproject.dto.UserCommentDTO;
import com.example.springbootpetproject.entity.Route;
import com.example.springbootpetproject.entity.User;
import com.example.springbootpetproject.service.serviceImplementation.RouteServiceI;
import com.example.springbootpetproject.service.serviceImplementation.UserCommentServiceI;
import com.example.springbootpetproject.service.serviceImplementation.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.springbootpetproject.controller.Paths.All_TRAINS_BETWEEN_CITIES_FILE;

@Controller
@RequestMapping("/train")
public class TrainController {
    private final UserServiceI userServiceI;
    private final UserCommentServiceI userCommentServiceI;
    private final RouteServiceI routeServiceI;

    @Autowired
    public TrainController(UserServiceI userServiceI, UserCommentServiceI userCommentServiceI, RouteServiceI routeServiceI) {
        this.userServiceI = userServiceI;
        this.userCommentServiceI = userCommentServiceI;
        this.routeServiceI = routeServiceI;
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
                routeDTOPage = routeServiceI.getAllWayBetweenCitiesWithTime(cityOfDeparture,cityOfArrival,selectedDatesString,selectedTimeString,pageable,pageNumber,direction,sort);
            } catch (RouteNotFound e) {
                model.addAttribute("RouteNotFound", e.getMessage());
                return "mainPage";
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
    public String getInfoAboutChangedTrain(@RequestParam("id") String id, Model model, Principal principal, HttpSession session){
        if(session.getAttribute("username")==null){
            User user = userServiceI.findUserByUsername(principal.getName());
            session.setAttribute("username",principal.getName());
            session.setAttribute("role",user.getUserRole().name());
            session.setAttribute("balance",user.getUserCountOfMoney());
        }
        Route route = routeServiceI.findRouteById(Long.parseLong(id));
        List<String> userCommentsList = userCommentServiceI
                .findByTrainNumber(route.getTrain().getTrainNumber())
                .stream()
                .map(UserCommentDTO::getUserComment)
                .collect(Collectors.toList());
        model.addAttribute("userCommentsList",userCommentsList);
        model.addAttribute("selectedRoute",route);
        return "trainInfo";
    }

}


