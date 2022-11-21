package com.example.springbootpetproject.service.serviceImplementation;


import com.example.springbootpetproject.customExceptions.userExceptions.InsufficientFunds;
import com.example.springbootpetproject.dto.OrderDTO;
import com.example.springbootpetproject.dto.RouteDTO;
import com.example.springbootpetproject.entity.*;
import com.example.springbootpetproject.facade.OrderFacade;
import com.example.springbootpetproject.facade.RouteFacade;
import com.example.springbootpetproject.service.serviceInterfaces.NumberOfPurchasedTicketsService;
import com.example.springbootpetproject.service.serviceInterfaces.RouteService;
import com.example.springbootpetproject.service.serviceInterfaces.TicketTypeService;
import com.example.springbootpetproject.service.serviceInterfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.springbootpetproject.repository.OrdersRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersServiceI implements com.example.springbootpetproject.service.serviceInterfaces.OrdersService {
    private final OrdersRepository ordersRepository;
    private final OrderFacade orderFacade;
    private final UserService userService;
    private final RouteService routeService;
    private final TicketTypeService ticketTypeService;
    private final NumberOfPurchasedTicketsService numberService;
    private final RouteFacade routeFacade;


    @Autowired
    public OrdersServiceI(OrdersRepository ordersRepository, OrderFacade orderFacade, UserService userService, RouteService routeService, TicketTypeService ticketTypeService, NumberOfPurchasedTicketsService numberService, RouteFacade routeFacade) {
        this.ordersRepository = ordersRepository;
        this.orderFacade = orderFacade;
        this.userService = userService;
        this.routeService = routeService;
        this.ticketTypeService = ticketTypeService;
        this.numberService = numberService;
        this.routeFacade = routeFacade;
    }

    @Override
    @Transactional
    public void addOrder(String username, String ticketCount, String place, String ticketType, Long routeID) throws InsufficientFunds {
        Order order = new Order();
        NumberOfPurchasedTicketsTypes ticketsCount = new NumberOfPurchasedTicketsTypes();

        Route selectedRoute = routeService.findRouteById(routeID);
        TicketType ticket = ticketTypeService.getTicketByTicketType(ticketType);
        order.setUser(userService.findUserByUsername(username));
        order.setRoute(selectedRoute);
        BigDecimal placePrice = BigDecimal.ONE;
        switch (place) {
            case "COMPARTMENT" -> placePrice = selectedRoute.getPriseOfCompartmentTicket();
            case "SUITE" -> placePrice = selectedRoute.getPriseOfSuiteTicket();
        }
        BigDecimal ticketPriseFactor = BigDecimal.valueOf(ticket.getTicketPriceFactor());
        BigDecimal orderPrice = placePrice.multiply(BigDecimal.valueOf(Long.parseLong(ticketCount))).multiply(ticketPriseFactor);
        order.setOrderPrise(orderPrice);
        userService.spendMoney(orderPrice,username);


        ordersRepository.save(order);

        switch (place) {
            case "COMPARTMENT" -> routeService.reduceNumberOfCompartmentFreeSeats(routeID,Integer.parseInt(ticketCount));
            case "SUITE" -> routeService.reduceNumberOfSuiteFreeSeats(routeID,Integer.parseInt(ticketCount));
        }

        ticketsCount.setPlace(TrainPlace.valueOf(place));
        ticketsCount.setTicketType(ticket);
        ticketsCount.setCountOfPurchasedTickets(Integer.parseInt(ticketCount));
        ticketsCount.setOrder(order);
        numberService.addNumberOfPurchasedTickets(ticketsCount);
    }

    @Override
    @Transactional
    public void addMultipleOrder(String username, List<NumberOfPurchasedTicketsTypes> ticketsTypesList,
             String ticketCount, String place, String ticketType, Long routeID) throws InsufficientFunds {
        Order order = new Order();
        NumberOfPurchasedTicketsTypes ticketsCount = new NumberOfPurchasedTicketsTypes();

        Route selectedRoute = routeService.findRouteById(routeID);
        TicketType ticket = ticketTypeService.getTicketByTicketType(ticketType);
        ticketsCount.setPlace(TrainPlace.valueOf(place));
        ticketsCount.setTicketType(ticket);
        ticketsCount.setCountOfPurchasedTickets(Integer.parseInt(ticketCount));
        ticketsTypesList.add(ticketsCount);

        order.setUser(userService.findUserByUsername(username));
        order.setRoute(selectedRoute);
        BigDecimal orderPrice = BigDecimal.ONE;

        for (NumberOfPurchasedTicketsTypes ticketsNumber : ticketsTypesList){
            orderPrice = orderPrice.add(calculateAllPrice(routeID,ticketsNumber.getPlace().name(),ticketsNumber.getTicketType().getTicketTypeName(), String.valueOf(ticketsNumber.getCountOfPurchasedTickets())));
            switch (ticketsNumber.getPlace().name()) {
                case "COMPARTMENT" -> routeService.reduceNumberOfCompartmentFreeSeats(routeID,ticketsNumber.getCountOfPurchasedTickets());
                case "SUITE" -> routeService.reduceNumberOfSuiteFreeSeats(routeID,ticketsNumber.getCountOfPurchasedTickets());
            }
        }
        order.setOrderPrise(orderPrice);
        userService.spendMoney(orderPrice,username);

        ordersRepository.save(order);

        for (NumberOfPurchasedTicketsTypes ticketsNumber : ticketsTypesList){
            ticketsNumber.setOrder(order);
            numberService.addNumberOfPurchasedTickets(ticketsNumber);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        return ordersRepository.getOrdersById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllUserOrders(Long id) {
        return ordersRepository.getAllByUser_Id(id)
                .stream()
                .map(orderFacade::convertOrdersToOrdersDTO)
                .collect(Collectors.toList());
    }

    /*@Override
    @Transactional(readOnly = true)
    public List<Orders> getAllUserOrdersByUserName(String username) {
        return ordersRepository.getAllByUser_username(username);
    }*/
    //
    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("#username == authentication.principal.username")
    public Page<OrderDTO> getAllUserOrdersByUserName(String username, Pageable pageable, int pageNumber, String direction, String sort) {
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()
                ,direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        return ordersRepository.getAllByUser_username(username, changePageable)
                .map(orderFacade::convertOrdersToOrdersDTO);
    }

    @Override
    public boolean exitByUserNameAndTrainName(String userName, String trainName) {
        return ordersRepository.existsOrdersByUser_usernameAndRoute_Train_TrainNumber(userName, trainName);
    }

    @Override
    public boolean exitByUserNameAndOrderID(String userName, Long id) {
        return ordersRepository.existsOrdersByUser_usernameAndId(userName, id);
    }

    public BigDecimal calculateAllPrice(Long id, String place, String ticketType, String ticketCount){
        TicketType ticket = ticketTypeService.getTicketByTicketType(ticketType);
        BigDecimal priceFactor = BigDecimal.valueOf(ticket.getTicketPriceFactor());
        BigDecimal ticketCountValue = BigDecimal.valueOf(Long.parseLong(ticketCount));
        RouteDTO routeDTO = routeFacade.convertRouteToRouteDTO(routeService.findRouteById(id));
        BigDecimal placePrice = switch (place) {
            case "COMPARTMENT" -> routeDTO.getPriseOfCompartmentTicket();
            case "SUITE" -> routeDTO.getPriseOfSuiteTicket();
            default -> BigDecimal.ZERO;
        };
        return priceFactor.multiply(ticketCountValue).multiply(placePrice);
    }

}
