package com.example.springbootpetproject.controller;

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

import jakarta.servlet.http.HttpSession;
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
            ,@RequestParam("place") String place,@RequestParam("ticketType") String ticketType, HttpSession session){
        String username = principal.getName();
        // Нехватку средств и мест обрабатывает GlobalExceptionHandler: он возвращает
        // пользователя на эту же страницу с flash-сообщением. Раньше ошибка ловилась здесь
        // и клалась в модель перед редиректом, где модель уже не доступна шаблону.
        if(session.getAttribute("ticketsTypesList")!=null){
            List<NumberOfPurchasedTicketsTypes> ticketsTypesList = (List<NumberOfPurchasedTicketsTypes>) session.getAttribute("ticketsTypesList");
            ordersService.addMultipleOrder(username,ticketsTypesList,ticketCount,place,ticketType,id);
            session.removeAttribute("ticketsTypesList");
            session.removeAttribute("allPrice");
            updateBalanceInSession(session, username);
            return "redirect:/user";
        }
        ordersService.addOrder(username,ticketCount,place,ticketType,id);
        session.removeAttribute("ticketsTypesList");
        session.removeAttribute("allPrice");
        updateBalanceInSession(session, username);
        return "redirect:/user";
    }

    /**
     * Баланс перечитывается из БД после покупки. Раньше в сессию попадало значение из
     * объекта User, загруженного до списания, — нативный UPDATE его не обновляет,
     * и в шапке висела старая сумма.
     */
    private void updateBalanceInSession(HttpSession session, String username) {
        session.setAttribute("balance", userServiceI.findUserByUsername(username).getUserCountOfMoney());
    }

    @PostMapping(path = "/{id}", params = "add")
    public String addOrder(@PathVariable("id") Long id, HttpSession session
            ,@RequestParam("place") String place, @RequestParam("ticketType") String ticketType,  @RequestParam("ticketsCount") String ticketCount){
        // Цену считаем первой: calculateAllPrice валидирует place и ticketsCount и бросает
        // ProblemWithSeatsCount, поэтому стоящие ниже valueOf/parseInt уже безопасны.
        // Раньше они выполнялись раньше любой проверки и давали 500 на кривом вводе.
        BigDecimal addedPrice = ordersService.calculateAllPrice(id, place, ticketType, ticketCount);

        List<NumberOfPurchasedTicketsTypes> ticketsTypesList =
                (List<NumberOfPurchasedTicketsTypes>) session.getAttribute("ticketsTypesList");
        if(ticketsTypesList == null){
            ticketsTypesList = new ArrayList<>();
        }

        NumberOfPurchasedTicketsTypes tickets = new NumberOfPurchasedTicketsTypes();
        tickets.setTicketType(ticketTypeServiceI.getTicketByTicketType(ticketType));
        tickets.setPlace(TrainPlace.valueOf(place));
        tickets.setCountOfPurchasedTickets(Integer.parseInt(ticketCount));
        ticketsTypesList.add(tickets);

        BigDecimal previousPrice = (BigDecimal) session.getAttribute("allPrice");
        BigDecimal allPrice = (previousPrice == null ? BigDecimal.ZERO : previousPrice).add(addedPrice);

        session.setAttribute("ticketsTypesList",ticketsTypesList);
        session.setAttribute("allPrice", allPrice);
        return "redirect:/order/"+id;
    }


}
