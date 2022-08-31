package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.entity.Train;
import com.example.springbootpetproject.entity.UserComments;
import com.example.springbootpetproject.service.serviceImplementation.TrainService;
import com.example.springbootpetproject.service.serviceImplementation.UserCommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;

import static com.example.springbootpetproject.controller.Paths.All_TRAINS_BETWEEN_CITIES_FILE;

@Controller
@RequestMapping("/train")
public class TrainController {

    private static final int DAYS_A_WEEK =7;

    public final TrainService trainService;
    public final UserCommentsService userCommentsService;

    @Autowired
    public TrainController(TrainService trainService, UserCommentsService userCommentsService) {
        this.trainService = trainService;
        this.userCommentsService = userCommentsService;
    }

    /*@GetMapping("/between/page/{pageNumber}")
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
    }*/

    @GetMapping("/between/page/{pageNumber}")
    public String getAllTrainBetweenCity(HttpServletRequest request, Model model
            ,@PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable
            ,@PathVariable("pageNumber") int pageNumber){

        String cityOfDeparture = request.getParameter("cityOfDeparture");
        String cityOfArrival = request.getParameter("cityOfArrival");
        String selectedDatesString = request.getParameter("selectedDates");
        String selectedTimeString = request.getParameter("selectedTime");

        LocalDate selectedDates = LocalDate.parse(selectedDatesString);
        LocalTime selectedTime = LocalTime.parse(selectedTimeString);
        LocalDateTime selectedLocalDateTime = LocalDateTime.of(selectedDates,selectedTime);
        //LocalDateTime finalLocalDateTime = getFinalDate(Date.valueOf(selectedDatesString));
        LocalDateTime finalLocalDateTime = selectedLocalDateTime.plusDays(7);

        Page<Train> page = trainService.getAllWayBetweenCitiesWithTime(cityOfDeparture,cityOfArrival,selectedLocalDateTime,finalLocalDateTime,pageable,pageNumber);
        List<Train> trainListContext = page.getContent();

        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageable",page);
        model.addAttribute("trainList",trainListContext);

        return All_TRAINS_BETWEEN_CITIES_FILE;
    }

    @GetMapping("/info")
    public String getInfoAboutChangedTrain(HttpServletRequest request, Model model){
        Train train = trainService.getTrainByName(request.getParameter("infoAboutTicket"));
        List<UserComments> userCommentsList = userCommentsService.findByTrainNumber(train.getTrainNumber());
        model.addAttribute("userCommentsList",userCommentsList);
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

    /*public LocalDateTime getFinalDate(String date){
        LocalDateTime localDateTime = LocalDateTime.parse(date);
        java.util.Date out = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(out);
        calendar.add(Calendar.DATE,DAYS_A_WEEK);
        LocalDateTime finalDates = LocalDateTime.ofInstant(calendar.getTime().toInstant(), ZoneId.systemDefault());
        return finalDates;
    }*/

    public LocalDateTime getFinalDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, DAYS_A_WEEK);
        LocalDateTime finalDates = LocalDateTime.ofInstant(calendar.getTime().toInstant(), ZoneId.systemDefault());
        return finalDates;
    }

}
