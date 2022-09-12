package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.entity.*;
import com.example.springbootpetproject.service.serviceImplementation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final OrdersService ordersService;
    private final UserCommentsService userCommentsService;
    private final TrainService trainService;
    private final CityService cityService;
    private final StationService stationService;
    private final RouteService routeService;

    @Autowired
    public AdminController(UserService userService, OrdersService ordersService, UserCommentsService userCommentsService, TrainService trainService, CityService cityService, StationService stationService, RouteService routeService) {
        this.userService = userService;
        this.ordersService = ordersService;
        this.userCommentsService = userCommentsService;
        this.trainService = trainService;
        this.cityService = cityService;
        this.stationService = stationService;
        this.routeService = routeService;
    }

    @GetMapping
    public String getAdminPage(){
        return "";
    }

    // User
    @GetMapping("/all/users/page/{pageNumber}")
    public String getAllUsersForAdmin(Model model
            , @PageableDefault(size = 2) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<User> userPage = userService.getAllUsers(pageable,pageNumber,direction,sort);
        List<User> userList = userPage.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",userPage);
        model.addAttribute("userList",userList);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");

        return "allUsers";
    }

    @GetMapping("/selectedUser/{userName}")
    public String getSelectedUser(Model model, @PathVariable("userName") String userName){
        User user = userService.findUserByUsername(userName);
        model.addAttribute("selectedUser", user);
        return "userInfoForAdmin";
    }

    @PostMapping("/changStatus/{userName}")
    public String changeUserStatus(HttpServletRequest request, @PathVariable("userName") String userName){
        userService.setUserVerification(userName);
        String pageNumber = request.getParameter("infoAboutPage");
        return "redirect:/admin/all/users/page/" + pageNumber;
    }

    @GetMapping("/selectedUser/order")
    public String getAllUserOrdersForAdmin(){
        return null;
    }

    @GetMapping("/selectedUser/{userName}/comment/page{pageNumber}")
    public String getAllUserCommentsForAdmin(@PathVariable("userName") String userName, Model model
            , Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<UserComments> userCommentsPage = userCommentsService.findAllUserComments(userName,pageable,pageNumber,direction,sort);
        List<UserComments> userCommentsList = userCommentsPage.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",userCommentsPage);
        model.addAttribute("userCommentsList",userCommentsList);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");
        return "allUserComments";
    }

    @PostMapping("/selectedUser/comment/{id}")
    public String deleteUserComment(Model model,@PathVariable("id")Long id){

        return null;
    }

    // Trains
    @GetMapping("/all/trains/page/{pageNumber}")
    public String getAllTrainsForAdmin(Model model
            , @PageableDefault(size = 10) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<Train> trainPage = trainService.getAllTrain(pageable,pageNumber,direction,sort);
        List<Train> trainList = trainPage.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",trainPage);
        model.addAttribute("trainList",trainList);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");
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
    public String updateInfoAboutTrain(@RequestParam("id") Long id, @RequestParam("trainNumber") String trainNumber
            ,@RequestParam("numberOfCompartmentSeats") int numberOfCompartmentSeats
            ,@RequestParam("numberOfSuiteSeats") int numberOfSuiteSeats){
        Train oldTrain = trainService.findTrainByID(id);
        oldTrain.setTrainNumber(trainNumber);
        oldTrain.setNumberOfCompartmentSeats(numberOfCompartmentSeats);
        oldTrain.setNumberOfSuiteSeats(numberOfSuiteSeats);
        trainService.updateTrain(oldTrain);
        return "redirect:/admin/train/" + id;
    }

    @GetMapping("/add/train")
    public String getPageToAddTrain(){
        return "addTrain";
    }

    @PostMapping("/add/train")
    public String addTrain(@RequestParam("trainNumber") String trainNumber
            ,@RequestParam("numberOfCompartmentSeats") int numberOfCompartmentSeats
            ,@RequestParam("numberOfSuiteSeats") int numberOfSuiteSeats){
        Train newTrain = new Train();
        newTrain.setTrainNumber(trainNumber);
        newTrain.setNumberOfCompartmentSeats(numberOfCompartmentSeats);
        newTrain.setNumberOfSuiteSeats(numberOfSuiteSeats);
        trainService.addTrain(newTrain);
        return "redirect:/admin/all/trains/page/1";
    }

    @PostMapping("/delete/train/{id}")
    public String deleteTrain(@PathVariable("id") Long id){
        trainService.deleteTrainByID(id);
        return "redirect:/admin/all/trains/page/1";
    }

    //Route
    @GetMapping("/all/route/page/{pageNumber}")
    public String getAllRouteForAdmin(Model model
            , @PageableDefault(size = 2) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<Route> routePage = routeService.getAll(pageable,pageNumber,direction,sort);
        List<Route> routeList = routePage.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",routePage);
        model.addAttribute("routeList",routeList);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");
        return "allRoute";
    }

    @PostMapping("/delete/route/{id}")
    public String deleteRoute(@PathVariable("id") Long id){
        routeService.deleteRoute(id);
        return "redirect:/admin/all/route/page/1";
    }

    @GetMapping("/route/change/{id}")
    public String getRouteForAdminByID(Model model, @PathVariable("id") Long id){
        Route selectedRoute = routeService.findById(id);
        model.addAttribute("selectedRoute",selectedRoute);
        return "changeRouteDetails";
    }

    @PostMapping("/route/change/{id}")
    public String updateRoute(@RequestParam Map<String,String> allParam,@PathVariable("id") Long id){
        routeService.updateRoute(allParam);
        return "redirect:/admin/route/change/" + id;
    }

    @GetMapping("/add/route")
    public String getPageToAddRoute(){
        return "addRoute";
    }

    @PostMapping("/add/route")
    public String addRoute(@RequestParam Map<String,String> allParam){
        routeService.addRoute(allParam);
        return "redirect:/admin/all/route/page/1";
    }

    @GetMapping("/find/route")
    public String findRouteByID(Model model, @RequestParam("id") Long id){
        Route selectedRoute = routeService.findById(id);
        model.addAttribute("selectedRoute",selectedRoute);
        return "changeRouteDetails";
    }

    // Cites and Stations
    @GetMapping("/all/cites/page/{pageNumber}")
    public String getAllCites(Model model
            , @PageableDefault(size = 10) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<City> cityPage = cityService.findAllCity(pageable,pageNumber,direction,sort);
        List<City> cityList = cityPage.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",cityPage);
        model.addAttribute("cityList",cityList);

        model.addAttribute("sort",sort);
        model.addAttribute("direction",direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");

        return "allCitesForAdmin";
    }

    @GetMapping("/all/stations/{cityName}/page/{pageNumber}")
    public String getAllStations(Model model
            , @PageableDefault(size = 10) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber, @PathVariable("cityName") String cityName
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<Station> stationPage = stationService.getAllStationInCity(cityName,pageable,pageNumber,direction,sort);
        List<Station> stationList = stationPage.getContent();
        model.addAttribute("cityName",cityName);
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",stationPage);
        model.addAttribute("stationList",stationList);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");
        return "allStations";
    }

    @GetMapping("/find/City")
    public String findCity(@RequestParam("cityName")String cityName){
        return "redirect:/admin/all/stations/" + cityName + "/page/1";
    }

    @GetMapping("/add/newCity")
    public String pageAddNewCity(){
        return "addCity";
    }

    @PostMapping("/add/newCity")
    public String addNewCity(@RequestParam("cityName") String cityName){
        City newCity = new City();
        newCity.setCityName(cityName);
        cityService.addCity(newCity);
        return "redirect:/admin/all/stations/" + newCity.getCityName() + "/page/1";
    }

    @PostMapping("/delete/{cityName}/station/{id}")
    public String deleteStation(@PathVariable("id") Long id, @PathVariable("cityName") String cityName){
        stationService.deleteStation(id);
        return "redirect:/admin/all/cites/page/1";
    }

    @GetMapping("/add/station/{cityName}")
    public String pageAddStation(@PathVariable("cityName") String cityName, Model model){
        model.addAttribute("cityName",cityName);
        return "addStation";
    }

    @GetMapping("/find/station")
    public String findStation(@RequestParam("stationName")String stationName, Model model){
        Station finderStation = stationService.findStationByStationName(stationName);
        model.addAttribute("finderStation",finderStation);
        return "updateStation";
    }

    @PostMapping("/add/station")
    public String addStation(HttpServletRequest request){
        Station newStation = new Station();
        String cityName = request.getParameter("cityName");
        newStation.setStationName(request.getParameter("stationName"));
        newStation.setCity(cityService.findByCityName(cityName));
        stationService.addStation(newStation);
        return "redirect:/all/stations/"+cityName+"/page/{pageNumber}";
    }

    @PostMapping("/update/station")
    public String updateStation(@RequestParam("cityName") String cityName
            , @RequestParam("stationName") String stationName, @RequestParam("id") Long id){
        Station updateStation = stationService.findByID(id);
        updateStation.setStationName(stationName);
        updateStation.setCity(cityService.findByCityName(cityName));
        stationService.updateStation(updateStation);
        return "redirect:/all/stations/"+cityName+"/page/{pageNumber}";
    }



}
