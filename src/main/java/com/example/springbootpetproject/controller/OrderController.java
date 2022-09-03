package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.entity.Orders;
import com.example.springbootpetproject.entity.TicketType;
import com.example.springbootpetproject.entity.Train;
import com.example.springbootpetproject.service.serviceImplementation.TicketTypeService;
import com.example.springbootpetproject.service.serviceImplementation.TrainService;
import com.example.springbootpetproject.service.serviceImplementation.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final TrainService trainService;
    private final TicketTypeService ticketTypeService;
    private final UserService userService;

    @Autowired
    public OrderController(TrainService trainService, TicketTypeService ticketTypeService, UserService userService) {
        this.trainService = trainService;
        this.ticketTypeService = ticketTypeService;
        this.userService = userService;
    }

    @GetMapping("/{trainNumber}")
    public String goToOrder(@PathVariable("trainNumber") String trainNumber, Model model){
        Train selectedTrain = trainService.getTrainByName(trainNumber);
        List<TicketType> ticketTypeList = ticketTypeService.getAllTicketTypes();
        model.addAttribute("selectedTrain",selectedTrain);
        model.addAttribute("countSeats",selectedTrain.getNumberOfFreeSeats());
        model.addAttribute("ticketTypeList",ticketTypeList);
        return "issuingTicket";
    }

    @PostMapping("/{trainNumber}")
    public String makeAnOrder(@PathVariable("trainNumber") String trainNumber, Principal principal){
        Orders order = new Orders();

        return "redirect:/order/"+trainNumber;
    }
}
