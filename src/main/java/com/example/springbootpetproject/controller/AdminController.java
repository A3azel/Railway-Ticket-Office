package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.entity.Train;
import com.example.springbootpetproject.entity.User;
import com.example.springbootpetproject.service.serviceImplementation.OrdersService;
import com.example.springbootpetproject.service.serviceImplementation.TrainService;
import com.example.springbootpetproject.service.serviceImplementation.UserCommentsService;
import com.example.springbootpetproject.service.serviceImplementation.UserService;
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

    @Autowired
    public AdminController(UserService userService, OrdersService ordersService, UserCommentsService userCommentsService, TrainService trainService) {
        this.userService = userService;
        this.ordersService = ordersService;
        this.userCommentsService = userCommentsService;
        this.trainService = trainService;
    }

    @GetMapping
    public String getAdminPage(){
        return "";
    }

    @GetMapping("/allUsers/page/{pageNumber}")
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

    @GetMapping("/allTrains/page/{pageNumber}")
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

    @GetMapping("/allStation")
    public String getAllStationForAdmin(){
        return null;
    }

    @GetMapping("/allTickets")
    public String getAllTicketsForAdmin(){
        return null;
    }

    @PostMapping("/changStatus/{userName}")
    public String changeUserStatus(HttpServletRequest request, @PathVariable("userName") String userName){
        userService.setUserVerification(userName);
        String pageNumber = request.getParameter("infoAboutPage");
        return "redirect:/admin/allUsers/page/" + pageNumber;

    }

    @GetMapping("/train/{id}")
    public String getTrainForAdmin(Model model, @PathVariable("id") String id){
        Train selectedTrain = trainService.findTrainByID(Long.parseLong(id));
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


}
