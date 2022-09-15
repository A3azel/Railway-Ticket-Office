package com.example.springbootpetproject.controller.adminCotrollers;

import com.example.springbootpetproject.dto.*;
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

//@Controller
//@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final OrdersService ordersService;
    private final UserCommentsService userCommentsService;
    private final TrainService trainService;
    private final CityService cityService;
    private final StationService stationService;
    private final RouteService routeService;

    //@Autowired
    public AdminController(UserService userService, OrdersService ordersService, UserCommentsService userCommentsService, TrainService trainService, CityService cityService, StationService stationService, RouteService routeService) {
        this.userService = userService;
        this.ordersService = ordersService;
        this.userCommentsService = userCommentsService;
        this.trainService = trainService;
        this.cityService = cityService;
        this.stationService = stationService;
        this.routeService = routeService;
    }

    //@GetMapping
    public String getAdminPage(){
        return "";
    }

    // User
    /*@GetMapping("/users/all/page/{pageNumber}")
    public String getAllUsersForAdmin(Model model
            , @PageableDefault(size = 2) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<UserDTO> userDTOPage = userService.getAllUsers(pageable,pageNumber,direction,sort);
        List<UserDTO> userList = userDTOPage.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",userDTOPage);
        model.addAttribute("userList",userList);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");

        return "allUsers";
    }

    @GetMapping("/users/selectedUser/{userName}")
    public String getSelectedUser(Model model, @PathVariable("userName") String userName){
        User user = userService.findUserByUsername(userName);
        model.addAttribute("selectedUser", user);
        return "userInfoForAdmin";
    }

    @PostMapping("/users/selectedUser/changStatus/{userName}")
    public String changeUserStatus(HttpServletRequest request, @PathVariable("userName") String userName){
        userService.setUserVerification(userName);
        String pageNumber = request.getParameter("infoAboutPage");
        return "redirect:/admin/users/all/page/" + pageNumber;
    }

    @GetMapping("/users/selectedUser/order")
    public String getAllUserOrdersForAdmin(){
        return null;
    }

    @GetMapping("/users/selectedUser/{userName}/comment/page/{pageNumber}")
    public String getAllUserCommentsForAdmin(@PathVariable("userName") String userName, Model model
            , Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<UserCommentsDTO> userCommentsDTOPage = userCommentsService.findAllUserComments(userName,pageable,pageNumber,direction,sort);
        List<UserCommentsDTO> userCommentsList = userCommentsDTOPage.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",userCommentsDTOPage);
        model.addAttribute("userCommentsList",userCommentsList);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");
        return "allUserComments";
    }

    @PostMapping("/users/selectedUser/comment/{id}")
    public String deleteUserComment(Model model,@PathVariable("id")Long id){

        return null;
    }
*/
    // Trains
   /* @GetMapping("/train/all/page/{pageNumber}")
    public String getAllTrainsForAdmin(Model model
            , @PageableDefault(size = 10) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<TrainDTO> trainDTOPage = trainService.getAllTrain(pageable,pageNumber,direction,sort);
        List<TrainDTO> trainList = trainDTOPage.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",trainDTOPage);
        model.addAttribute("trainList",trainList);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");
        return "allTrainsForAdmin";
    }

    @GetMapping("/train/{id}")
    public String getTrainForAdminByID(Model model, @PathVariable("id") String id){
        Train train = trainService.findTrainByID(Long.parseLong(id));
        TrainDTO selectedTrain = trainService.convertTrainToTrainDTO(train);
        model.addAttribute("selectedTrain",selectedTrain);
        return "changeTrainDetails";
    }

    @GetMapping("/train/find/byTrainNumber")
    public String getTrainForAdminByTrainNumber(HttpServletRequest request,Model model){
        String trainNumber = request.getParameter("wantedTrain");
        Train train = trainService.findTrainByTrainNumber(trainNumber);
        TrainDTO selectedTrain = trainService.convertTrainToTrainDTO(train);
        model.addAttribute("selectedTrain",selectedTrain);
        return "changeTrainDetails";
    }

    @PostMapping("/train/update")
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

    @GetMapping("/train/add")
    public String getPageToAddTrain(){
        return "addTrain";
    }

    @PostMapping("/train/add")
    public String addTrain(@RequestParam("trainNumber") String trainNumber
            ,@RequestParam("numberOfCompartmentSeats") int numberOfCompartmentSeats
            ,@RequestParam("numberOfSuiteSeats") int numberOfSuiteSeats){
        Train newTrain = new Train();
        newTrain.setTrainNumber(trainNumber);
        newTrain.setNumberOfCompartmentSeats(numberOfCompartmentSeats);
        newTrain.setNumberOfSuiteSeats(numberOfSuiteSeats);
        trainService.addTrain(newTrain);
        return "redirect:/admin/train/all/page/1";
    }

    @PostMapping("/train/delete/{id}")
    public String deleteTrain(@PathVariable("id") Long id){
        trainService.deleteTrainByID(id);
        return "redirect:/admin/train/all/page/1";
    }*/

    //Route
/*    @GetMapping("/route/all/page/{pageNumber}")
    public String getAllRouteForAdmin(Model model
            , @PageableDefault(size = 2) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<RouteDTO> routeDTOPage = routeService.getAll(pageable,pageNumber,direction,sort);
        List<RouteDTO> routeDTOList = routeDTOPage.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",routeDTOPage);
        model.addAttribute("routeDTOList",routeDTOList);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");
        return "allRoute";
    }

    @PostMapping("/route/delete/{id}")
    public String deleteRoute(@PathVariable("id") Long id){
        routeService.deleteRoute(id);
        return "redirect:/admin/route/all/page/1";
    }

    @GetMapping("/route/update/{id}")
    public String getRouteForAdminByID(Model model, @PathVariable("id") Long id){
        Route route = routeService.findById(id);
        RouteDTO selectedRoute = routeService.convertRouteToRouteDTO(route);
        model.addAttribute("selectedRoute",selectedRoute);
        return "changeRouteDetails";
    }

    @PostMapping("/route/update/{id}")
    public String updateRoute(@RequestParam Map<String,String> allParam,@PathVariable("id") Long id){
        routeService.updateRoute(allParam);
        return "redirect:/admin/route/update/" + id;
    }

    @GetMapping("/route/add")
    public String getPageToAddRoute(){
        return "addRoute";
    }

    @PostMapping("/route/add")
    public String addRoute(@RequestParam Map<String,String> allParam){
        routeService.addRoute(allParam);
        return "redirect:/admin/all/route/page/1";
    }

    @GetMapping("/route/find")
    public String findRouteByID(Model model, @RequestParam("id") Long id){
        Route route = routeService.findById(id);
        RouteDTO selectedRoute = routeService.convertRouteToRouteDTO(route);
        model.addAttribute("selectedRoute",selectedRoute);
        return "changeRouteDetails";
    }*/

    // Cites and Stations
    /*@GetMapping("/city/all/page/{pageNumber}")
    public String getAllCites(Model model
            , @PageableDefault(size = 10) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<CityDTO> cityPage = cityService.findAllCity(pageable,pageNumber,direction,sort);
        List<CityDTO> cityList = cityPage.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",cityPage);
        model.addAttribute("cityList",cityList);

        model.addAttribute("sort",sort);
        model.addAttribute("direction",direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");

        return "allCitesForAdmin";
    }

    @GetMapping("/station/all/{cityName}/page/{pageNumber}")
    public String getAllStations(Model model
            , @PageableDefault(size = 10) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber, @PathVariable("cityName") String cityName
            , @RequestParam(required = false, defaultValue = "asc", value = "direction") String direction
            , @RequestParam(required = false, defaultValue = "id",value = "sort") String sort){
        Page<StationDTO> stationDTOPage = stationService.getAllStationInCity(cityName,pageable,pageNumber,direction,sort);
        List<StationDTO> stationDTOList = stationDTOPage.getContent();
        model.addAttribute("cityName",cityName);
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",stationDTOPage);
        model.addAttribute("stationDTOList",stationDTOList);

        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");
        return "allStations";
    }

    @GetMapping("/city/find")
    public String findCity(@RequestParam("cityName")String cityName){
        return "redirect:/admin/all/stations/" + cityName + "/page/1";
    }

    @GetMapping("/city/add")
    public String pageAddNewCity(){
        return "addCity";
    }

    @PostMapping("/city/add")
    public String addNewCity(@RequestParam("cityName") String cityName){
        City newCity = new City();
        newCity.setCityName(cityName);
        cityService.addCity(newCity);
        return "redirect:/admin/station/all/" + newCity.getCityName() + "/page/1";
    }

    @PostMapping("/station/delete/{cityName}/{id}")
    public String deleteStation(@PathVariable("id") Long id, @PathVariable("cityName") String cityName){
        stationService.deleteStation(id);
        return "redirect:/admin/city/all/page/1";
    }

    @GetMapping("/station/add/{cityName}")
    public String pageAddStation(@PathVariable("cityName") String cityName, Model model){
        model.addAttribute("cityName",cityName);
        return "addStation";
    }

    @GetMapping("/station/find")
    public String findStation(@RequestParam("stationName")String stationName, Model model){
        Station station = stationService.findStationByStationName(stationName);
        StationDTO finderStation = stationService.convertStationToStationDTO(station);
        model.addAttribute("finderStation",finderStation);
        return "updateStation";
    }

    @PostMapping("/station/add")
    public String addStation(HttpServletRequest request){
        Station newStation = new Station();
        String cityName = request.getParameter("cityName");
        newStation.setStationName(request.getParameter("stationName"));
        newStation.setCity(cityService.findByCityName(cityName));
        stationService.addStation(newStation);
        return "redirect:/admin/station/all/"+cityName+"/page/1";
    }

    @PostMapping("/station/update")
    public String updateStation(@RequestParam("cityName") String cityName
            , @RequestParam("stationName") String stationName, @RequestParam("id") Long id){
        Station updateStation = stationService.findByID(id);
        updateStation.setStationName(stationName);
        updateStation.setCity(cityService.findByCityName(cityName));
        stationService.updateStation(updateStation);
        return "redirect:/admin/station/all/"+cityName+"/page/1";
    }
*/

}
