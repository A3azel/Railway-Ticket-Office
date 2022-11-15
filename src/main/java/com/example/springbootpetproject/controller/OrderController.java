package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.dto.TicketTypeDTO;
import com.example.springbootpetproject.entity.Order;
import com.example.springbootpetproject.entity.Route;
import com.example.springbootpetproject.service.serviceImplementation.RouteServiceI;
import com.example.springbootpetproject.service.serviceImplementation.TicketTypeServiceI;
import com.example.springbootpetproject.service.serviceImplementation.TrainServiceI;
import com.example.springbootpetproject.service.serviceImplementation.UserServiceI;
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
    private final TrainServiceI trainServiceI;
    private final TicketTypeServiceI ticketTypeServiceI;
    private final UserServiceI userServiceI;
    private final RouteServiceI routeServiceI;

    @Autowired
    public OrderController(TrainServiceI trainServiceI, TicketTypeServiceI ticketTypeServiceI, UserServiceI userServiceI, RouteServiceI routeServiceI) {
        this.trainServiceI = trainServiceI;
        this.ticketTypeServiceI = ticketTypeServiceI;
        this.userServiceI = userServiceI;
        this.routeServiceI = routeServiceI;
    }

    @GetMapping("/{id}")
    public String goToOrder(@PathVariable("id") Long id, Model model){
        Route selectedRoute = routeServiceI.findRouteById(id);
        List<TicketTypeDTO> ticketTypeDTOList = ticketTypeServiceI.getAllTicketTypesForOrder();
        model.addAttribute("selectedRoute",selectedRoute);
        model.addAttribute("ticketTypeList",ticketTypeDTOList);
        return "issuingTicket";
    }

    @PostMapping("/{id}")
    public String makeAnOrder(@PathVariable("id") Long id, Principal principal){
        Order order = new Order();

        return "redirect:/order/"+id;
    }
}
