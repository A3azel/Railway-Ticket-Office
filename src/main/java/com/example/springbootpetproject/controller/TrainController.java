package com.example.springbootpetproject.controller;

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

    private static final int DAYS_A_WEEK = 7;

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

        Page<Route> routePage = routeService.getAllWayBetweenCitiesWithTime(cityOfDeparture,cityOfArrival,selectedDatesString,selectedTimeString,pageable,pageNumber,direction,sort);
        List<Route> routeList = routePage.getContent();

        model.addAttribute("selectedDates",selectedDatesString);
        model.addAttribute("selectedTime",selectedTimeString);
        model.addAttribute("cityOfDeparture",cityOfDeparture);
        model.addAttribute("cityOfArrival",cityOfArrival);

        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",routePage);
        model.addAttribute("routeList",routeList);
        model.addAttribute("direction", direction.equals("asc") ? "desc" : "asc");

        return All_TRAINS_BETWEEN_CITIES_FILE;
    }

    @GetMapping("/info")
    public String getInfoAboutChangedTrain(@RequestParam("id") String id, Model model){
        Route route = routeService.findById(Long.parseLong(id));
        List<UserComments> userCommentsList = userCommentsService.findByTrainNumber(route.getTrain().getTrainNumber());
        model.addAttribute("userCommentsList",userCommentsList);
        model.addAttribute("selectedRoute",route);
        return "trainInfo";
    }

}


/*@Controller
@RequestMapping("/train")
public class TrainController {

    private static final int DAYS_A_WEEK =7;

    public final TrainService trainService;
    public final UserCommentsService userCommentsService;
    public final RouteService routeService;

    @Autowired
    public TrainController(TrainService trainService, UserCommentsService userCommentsService, RouteService routeService) {
        this.trainService = trainService;
        this.userCommentsService = userCommentsService;
        this.routeService = routeService;
    }

    *//*@GetMapping("/between/page/{pageNumber}")
    public String getAllTrainBetweenCity(HttpServletRequest request, Model model, Principal principal
            ,@PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable
            ,@PathVariable("pageNumber") int pageNumber){

        String cityOfDeparture = request.getParameter("cityOfDeparture");
        String cityOfArrival = request.getParameter("cityOfArrival");
        String selectedDatesString = request.getParameter("selectedDates");
        LocalDateTime selectedDates = LocalDateTime.parse(selectedDatesString);
        LocalDateTime finalDates = getFinalDate(selectedDatesString);
        Page<Train> page = trainService.getAllWayBetweenCitiesWithTime(cityOfDeparture,cityOfArrival,selectedDates,finalDates,pageable,pageNumber);
        List<Train> trainListContext = page.getContent();
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",page);
        model.addAttribute("trainList",trainListContext);
        System.out.println(selectedDates);
        System.out.println(finalDates);
        return All_TRAINS_BETWEEN_CITIES_FILE;
    }*//*

    @GetMapping("/between/page/{pageNumber}")
    public String getAllTrainBetweenCity(Model model
            , @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable
            , @PathVariable("pageNumber") int pageNumber
            , @RequestParam("cityOfDeparture") String cityOfDeparture, @RequestParam("cityOfArrival") String cityOfArrival
            ,@RequestParam("selectedDates") String selectedDatesString,@RequestParam("selectedTime") String selectedTimeString){

        Page<Route> page = routeService.getAllWayBetweenCitiesWithTime(cityOfDeparture,cityOfArrival,selectedDatesString,selectedTimeString,pageable,pageNumber);
        List<Route> trainListContext = page.getContent();

        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",page);
        model.addAttribute("trainList",trainListContext);

        return All_TRAINS_BETWEEN_CITIES_FILE;
    }

    *//*@GetMapping("/info")
    public String getInfoAboutChangedTrain(HttpServletRequest request, Model model){
        Train train = trainService.getTrainByName(request.getParameter("infoAboutTicket"));
        List<UserComments> userCommentsList = userCommentsService.findByTrainNumber(train.getTrainNumber());
        model.addAttribute("userCommentsList",userCommentsList);
        model.addAttribute("selectedTrain",train);
        return "trainInfo";
    }*//*

    *//*public LocalDateTime getFinalDate(String date){
        LocalDateTime localDateTime = LocalDateTime.parse(date);
        java.util.Date out = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(out);
        calendar.add(Calendar.DATE,DAYS_A_WEEK);
        LocalDateTime finalDates = LocalDateTime.ofInstant(calendar.getTime().toInstant(), ZoneId.systemDefault());
        return finalDates;
    }*//*

    *//*public LocalDateTime getFinalDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, DAYS_A_WEEK);
        LocalDateTime finalDates = LocalDateTime.ofInstant(calendar.getTime().toInstant(), ZoneId.systemDefault());
        return finalDates;
    }*//*

}*/
