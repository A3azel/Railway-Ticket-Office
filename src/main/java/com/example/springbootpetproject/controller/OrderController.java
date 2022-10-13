package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.dto.TicketTypeDTO;
import com.example.springbootpetproject.entity.Orders;
import com.example.springbootpetproject.entity.Route;
import com.example.springbootpetproject.entity.TicketType;
import com.example.springbootpetproject.entity.Train;
import com.example.springbootpetproject.service.serviceImplementation.RouteService;
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
    private final RouteService routeService;

    @Autowired
    public OrderController(TrainService trainService, TicketTypeService ticketTypeService, UserService userService, RouteService routeService) {
        this.trainService = trainService;
        this.ticketTypeService = ticketTypeService;
        this.userService = userService;
        this.routeService = routeService;
    }

    @GetMapping("/{id}")
    public String goToOrder(@PathVariable("id") Long id, Model model){
        Route selectedRoute = routeService.findRouteById(id);
        List<TicketTypeDTO> ticketTypeDTOList = ticketTypeService.getAllTicketTypesForOrder();
        model.addAttribute("selectedRoute",selectedRoute);
        model.addAttribute("ticketTypeList",ticketTypeDTOList);
        return "issuingTicket";
    }

    @PostMapping("/{id}")
    public String makeAnOrder(@PathVariable("id") Long id, Principal principal){
        Orders order = new Orders();

        return "redirect:/order/"+id;
    }
}
