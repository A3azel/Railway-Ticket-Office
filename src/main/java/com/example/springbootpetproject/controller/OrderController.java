package com.example.springbootpetproject.controller;

import com.example.springbootpetproject.customExceptions.userExceptions.InsufficientFunds;
import com.example.springbootpetproject.dto.RouteDTO;
import com.example.springbootpetproject.dto.TicketTypeDTO;
import com.example.springbootpetproject.entity.*;
import com.example.springbootpetproject.facade.RouteFacade;
import com.example.springbootpetproject.service.serviceImplementation.RouteServiceI;
import com.example.springbootpetproject.service.serviceImplementation.TicketTypeServiceI;
import com.example.springbootpetproject.service.serviceImplementation.TrainServiceI;
import com.example.springbootpetproject.service.serviceImplementation.UserServiceI;
import com.example.springbootpetproject.service.serviceInterfaces.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final TicketTypeServiceI ticketTypeServiceI;
    private final UserServiceI userServiceI;
    private final RouteServiceI routeServiceI;
    private final RouteFacade routeFacade;
    private final OrdersService ordersService;

    @Autowired
    public OrderController(TicketTypeServiceI ticketTypeServiceI, UserServiceI userServiceI, RouteServiceI routeServiceI, RouteFacade routeFacade, OrdersService ordersService) {
        this.ticketTypeServiceI = ticketTypeServiceI;
        this.userServiceI = userServiceI;
        this.routeServiceI = routeServiceI;
        this.routeFacade = routeFacade;
        this.ordersService = ordersService;
    }

    @GetMapping("/{id}")
    public String goToOrder(@PathVariable("id") Long id, Model model, Principal principal, HttpSession session){
        User user = userServiceI.findUserByUsername(principal.getName());
        if(session.getAttribute("username")==null){
            session.setAttribute("username",principal.getName());
            session.setAttribute("role",user.getUserRole().name());
            session.setAttribute("balance",user.getUserCountOfMoney());
        }
        Route selectedRoute = routeServiceI.findRouteById(id);
        List<TicketTypeDTO> ticketTypeDTOList = ticketTypeServiceI.getAllTicketTypesForOrder();
        //model.addAttribute("selectedRoute",selectedRoute);
        RouteDTO selectedRouteDTO = routeFacade.convertRouteToRouteDTO(selectedRoute);
        model.addAttribute("selectedRoute",selectedRouteDTO);
        model.addAttribute("ticketTypeList",ticketTypeDTOList);
        return "issuingTicket";
    }

    @PostMapping(path = "/{id}",params = "by")
    public String makeAnOrder(@PathVariable("id") Long id, Principal principal, @RequestParam("ticketsCount") String ticketCount
            ,@RequestParam("place") String place,@RequestParam("ticketType") String ticketType, Model model, HttpSession session){
        String username = principal.getName();
        User user = userServiceI.findUserByUsername(username);
        if(session.getAttribute("ticketsTypesList")!=null){
            List<NumberOfPurchasedTicketsTypes> ticketsTypesList = (List<NumberOfPurchasedTicketsTypes>) session.getAttribute("ticketsTypesList");
            try {
                ordersService.addMultipleOrder(username,ticketsTypesList,ticketCount,place,ticketType,id);
            } catch (InsufficientFunds e) {
                model.addAttribute("insufficientFunds", e.getMessage());
                return "redirect:/order/"+id;
            }

            session.removeAttribute("ticketsTypesList");
            session.setAttribute("balance",user.getUserCountOfMoney());
            return "redirect:/user";
        }
        try {
            ordersService.addOrder(username,ticketCount,place,ticketType,id);
        } catch (InsufficientFunds e) {
            model.addAttribute("insufficientFunds", e.getMessage());
            session.removeAttribute("ticketsTypesList");
            return "redirect:/order/"+id;
        }
        session.removeAttribute("ticketsTypesList");
        session.setAttribute("balance",user.getUserCountOfMoney());
        return "redirect:/user";
    }

    @PostMapping(path = "/{id}", params = "add")
    public String addOrder(@PathVariable("id") Long id, HttpSession session
            ,@RequestParam("place") String place, @RequestParam("ticketType") String ticketType,  @RequestParam("ticketsCount") String ticketCount){
        if(session.getAttribute("ticketsTypesList")==null){
            List<NumberOfPurchasedTicketsTypes> ticketsTypesList = new ArrayList<>();
            TicketType ticket = ticketTypeServiceI.getTicketByTicketType(ticketType);
            NumberOfPurchasedTicketsTypes tickets = new NumberOfPurchasedTicketsTypes();
            tickets.setTicketType(ticket);
            tickets.setPlace(TrainPlace.valueOf(place));
            tickets.setCountOfPurchasedTickets(Integer.parseInt(ticketCount));
            ticketsTypesList.add(tickets);
            BigDecimal price = ordersService.calculateAllPrice(id, place, ticketType, ticketCount);
            session.setAttribute("ticketsTypesList",ticketsTypesList);
            session.setAttribute("allPrice", price);
            return "redirect:/order/"+id;
        }
        List<NumberOfPurchasedTicketsTypes> ticketsTypesList = (List<NumberOfPurchasedTicketsTypes>) session.getAttribute("ticketsTypesList");
        NumberOfPurchasedTicketsTypes tickets = new NumberOfPurchasedTicketsTypes();
        tickets.setTicketType(ticketTypeServiceI.getTicketByTicketType(ticketType));
        tickets.setPlace(TrainPlace.valueOf(place));
        tickets.setCountOfPurchasedTickets(Integer.parseInt(ticketCount));
        ticketsTypesList.add(tickets);
        BigDecimal allPrice = BigDecimal.ZERO;
        for(NumberOfPurchasedTicketsTypes numberOfTickets: ticketsTypesList){
            allPrice = allPrice.add(ordersService.calculateAllPrice(id,numberOfTickets.getPlace().name(),numberOfTickets.getTicketType().getTicketTypeName(), String.valueOf(numberOfTickets.getCountOfPurchasedTickets())));
        }
        session.setAttribute("ticketsTypesList",ticketsTypesList);
        session.setAttribute("allPrice", allPrice);
        return "redirect:/order/"+id;
    }


}
