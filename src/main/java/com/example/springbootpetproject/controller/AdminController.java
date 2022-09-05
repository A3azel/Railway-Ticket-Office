package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.entity.City;
import com.example.springbootpetproject.entity.Station;
import com.example.springbootpetproject.entity.Train;
import com.example.springbootpetproject.entity.User;
import com.example.springbootpetproject.service.serviceImplementation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final OrdersService ordersService;
    private final UserCommentsService userCommentsService;
    private final TrainService trainService;
    private final CityService cityService;
    private final StationService stationService;

    @Autowired
    public AdminController(UserService userService, OrdersService ordersService, UserCommentsService userCommentsService, TrainService trainService, CityService cityService, StationService stationService) {
        this.userService = userService;
        this.ordersService = ordersService;
        this.userCommentsService = userCommentsService;
        this.trainService = trainService;
        this.cityService = cityService;
        this.stationService = stationService;
    }

    @GetMapping
    public String getAdminPage(){
        return "";
    }

    // User
    @GetMapping("/all/users/page/{pageNumber}")
    public String getAllUsersForAdmin(Model model
            , @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber){
        Page<User> userPage = userService.getAllUsers(pageable,pageNumber);
        List<User> userList = userPage.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",userPage);
        model.addAttribute("userList",userList);
        return "allUsers";
    }

    @GetMapping("/selectedUser/{userName}")
    public String getSelectedUser(Model model, @PathVariable("userName") String userName){
        User user = userService.findUserByUsername(userName);
        model.addAttribute("selectedUser", user);
        return "InfoAboutUserForAdmin";
    }

    @PostMapping("/changStatus/{userName}")
    public String changeUserStatus(HttpServletRequest request, @PathVariable("userName") String userName){
        userService.setUserVerification(userName);
        String pageNumber = request.getParameter("infoAboutPage");
        return "redirect:/admin/all/users/page/" + pageNumber;

    }

    // Trains
    @GetMapping("/all/trains/page/{pageNumber}")
    public String getAllTrainsForAdmin(Model model
            , @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber){
        Page<Train> trainPage = trainService.getAllTrain(pageable,pageNumber);
        List<Train> trainList = trainPage.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",trainPage);
        model.addAttribute("trainList",trainList);
        return "allTrainsForAdmin";
    }

    @GetMapping("/train/{id}")
    public String getTrainForAdminByID(Model model, @PathVariable("id") String id){
        Train selectedTrain = trainService.findTrainByID(Long.parseLong(id));
        model.addAttribute("selectedTrain",selectedTrain);
        return "changeTrainDetails";
    }

    @GetMapping("/train/find/byTrainNumber")
    public String getTrainForAdminByTrainNumber(HttpServletRequest request,Model model){
        String trainNumber = request.getParameter("wantedTrain");
        Train selectedTrain = trainService.findTrainByTrainNumber(trainNumber);
        model.addAttribute("selectedTrain",selectedTrain);
        return "changeTrainDetails";
    }

    @PostMapping("/updateInfoAboutTrain")
    public String updateInfoAboutTrain(HttpServletRequest request){
        String id = request.getParameter("id");
        String trainNumber = request.getParameter("trainNumber");
        String startStation = request.getParameter("startStation");
        String departureData = request.getParameter("departureData");
        String departureTime = request.getParameter("departureTime");
        String travelTime = request.getParameter("travelTime");
        String arrivalStation = request.getParameter("arrivalStation");
        String arrivalData = request.getParameter("arrivalData");
        String arrivalTime = request.getParameter("arrivalTime");
        String numberOfFreeSeats = request.getParameter("numberOfFreeSeats");
        String priseOfTicket = request.getParameter("priseOfTicket");
        trainService.updateTrain(id,trainNumber,startStation,departureData,departureTime,travelTime,arrivalStation,arrivalData,arrivalTime,numberOfFreeSeats,priseOfTicket);
        return "redirect:/admin/train/" + id;
    }

    @GetMapping("/add/train")
    public String getPageToAddTrain(){
        return "addTrain";
    }

    @PostMapping("/add/train")
    public String addTrain(HttpServletRequest request){
        String trainNumber = request.getParameter("trainNumber");
        String startStation = request.getParameter("startStation");
        String departureData = request.getParameter("departureData");
        String departureTime = request.getParameter("departureTime");
        String travelTime = request.getParameter("travelTime");
        String arrivalStation = request.getParameter("arrivalStation");
        String arrivalData = request.getParameter("arrivalData");
        String arrivalTime = request.getParameter("arrivalTime");
        String numberOfFreeSeats = request.getParameter("numberOfFreeSeats");
        String priseOfTicket = request.getParameter("priseOfTicket");
        trainService.addTrain(trainNumber,startStation,departureData,departureTime,
                travelTime,arrivalStation,arrivalData,arrivalTime,numberOfFreeSeats,priseOfTicket);
        return "redirect:/admin/all/trains/page/1";
    }

    @PostMapping("/delete/train/{id}")
    public String deleteTrain(@PathVariable("id") Long id){
        trainService.deleteTrainByID(id);
        return "redirect:/admin/all/trains/page/1";
    }


    // Cites and Stations
    @GetMapping("/all/cites/page/{pageNumber}")
    public String getAllCites(Model model
            , @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber){
        Page<City> cityPage = cityService.findAllCity(pageable,pageNumber);
        List<City> cityList = cityPage.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",cityPage);
        model.addAttribute("cityList",cityList);
        return "allCitesForAdmin";
    }

    @GetMapping("/all/stations/page/{pageNumber}")
    public String getAllStations(HttpServletRequest request,Model model
            , @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber){
        String city = request.getParameter("cityName");
        Page<Station> stationPage = stationService.getAllStationInCity(city,pageable,pageNumber);
        List<Station> stationList = stationPage.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",stationPage);
        model.addAttribute("stationList",stationList);
        return "allStations";
    }

    @GetMapping("/add/newCity")
    public String pageAddNewCity(){
        return null;
    }

    @PostMapping("/add/newCity")
    public String addNewCity(){
        return null;
    }



}
