package com.example.springbootpetproject.service.serviceImplementation;


import com.example.springbootpetproject.customExceptions.routeExceptions.ProblemWithSeatsCount;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersServiceI implements com.example.springbootpetproject.service.serviceInterfaces.OrdersService {
    /** Потолок на один заказ: защита от запроса вида ticketsCount=1000000 в обход формы. */
    private static final int MAX_TICKETS_PER_ORDER = 10;

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
        int ticketsNumber = parseTicketsCount(ticketCount);
        TrainPlace trainPlace = parsePlace(place);

        Route selectedRoute = routeService.findRouteById(routeID);
        TicketType ticket = ticketTypeService.getTicketByTicketType(ticketType);

        BigDecimal orderPrice = calculateAllPrice(routeID, trainPlace.name(), ticketType, String.valueOf(ticketsNumber));

        Order order = new Order();
        order.setUser(userService.findUserByUsername(username));
        order.setRoute(selectedRoute);
        order.setOrderPrise(orderPrice);

        chargeAndReserve(username, routeID, orderPrice, List.of(new SeatsToReserve(trainPlace, ticketsNumber)));

        ordersRepository.save(order);

        NumberOfPurchasedTicketsTypes ticketsCount = new NumberOfPurchasedTicketsTypes();
        ticketsCount.setPlace(trainPlace);
        ticketsCount.setTicketType(ticket);
        ticketsCount.setCountOfPurchasedTickets(ticketsNumber);
        ticketsCount.setOrder(order);
        numberService.addNumberOfPurchasedTickets(ticketsCount);
    }

    @Override
    @Transactional
    public void addMultipleOrder(String username, List<NumberOfPurchasedTicketsTypes> ticketsTypesList,
             String ticketCount, String place, String ticketType, Long routeID) throws InsufficientFunds {
        int ticketsNumber = parseTicketsCount(ticketCount);
        TrainPlace trainPlace = parsePlace(place);

        Route selectedRoute = routeService.findRouteById(routeID);
        TicketType ticket = ticketTypeService.getTicketByTicketType(ticketType);

        NumberOfPurchasedTicketsTypes lastAddedTickets = new NumberOfPurchasedTicketsTypes();
        lastAddedTickets.setPlace(trainPlace);
        lastAddedTickets.setTicketType(ticket);
        lastAddedTickets.setCountOfPurchasedTickets(ticketsNumber);
        ticketsTypesList.add(lastAddedTickets);

        // Начинаем с ZERO: было BigDecimal.ONE, из-за чего каждый мультизаказ
        // стоил на 1 больше суммы билетов.
        BigDecimal orderPrice = BigDecimal.ZERO;
        List<SeatsToReserve> seatsToReserve = new ArrayList<>();
        for (NumberOfPurchasedTicketsTypes tickets : ticketsTypesList){
            orderPrice = orderPrice.add(calculateAllPrice(routeID, tickets.getPlace().name(),
                    tickets.getTicketType().getTicketTypeName(), String.valueOf(tickets.getCountOfPurchasedTickets())));
            seatsToReserve.add(new SeatsToReserve(tickets.getPlace(), tickets.getCountOfPurchasedTickets()));
        }

        Order order = new Order();
        order.setUser(userService.findUserByUsername(username));
        order.setRoute(selectedRoute);
        order.setOrderPrise(orderPrice);

        chargeAndReserve(username, routeID, orderPrice, seatsToReserve);

        ordersRepository.save(order);

        for (NumberOfPurchasedTicketsTypes tickets : ticketsTypesList){
            tickets.setOrder(order);
            numberService.addNumberOfPurchasedTickets(tickets);
        }
    }

    /** Сколько мест какого типа нужно списать с маршрута. */
    private record SeatsToReserve(TrainPlace place, int count) {}

    /**
     * Списание денег и мест. Порядок (сначала счёт, потом места) одинаков во всех
     * методах сознательно: параллельные заказы захватывают строки в одной
     * последовательности и не встают в deadlock. Раньше в мультизаказе места
     * уменьшались до проверки средств и при нехватке денег оставались списанными.
     */
    private void chargeAndReserve(String username, Long routeID, BigDecimal orderPrice,
                                  List<SeatsToReserve> seatsToReserve) throws InsufficientFunds {
        userService.spendMoney(orderPrice, username);
        for (SeatsToReserve seats : seatsToReserve){
            switch (seats.place()) {
                case COMPARTMENT -> routeService.reduceNumberOfCompartmentFreeSeats(routeID, seats.count());
                case SUITE -> routeService.reduceNumberOfSuiteFreeSeats(routeID, seats.count());
                default -> throw new ProblemWithSeatsCount("Unsupported place type: " + seats.place());
            }
        }
    }

    private int parseTicketsCount(String ticketCount) {
        int parsed;
        try {
            parsed = Integer.parseInt(ticketCount);
        } catch (NumberFormatException e) {
            throw new ProblemWithSeatsCount("The number of tickets must be an integer");
        }
        if(parsed <= 0 || parsed > MAX_TICKETS_PER_ORDER){
            throw new ProblemWithSeatsCount("The number of tickets must be between 1 and " + MAX_TICKETS_PER_ORDER);
        }
        return parsed;
    }

    /**
     * TrainPlace содержит служебное значение S, которое не является покупаемым типом места:
     * с ним цена считалась по ветке default (то есть ноль), а места не списывались вовсе.
     * Поэтому проверяем не только принадлежность enum, но и сам тип.
     */
    private TrainPlace parsePlace(String place) {
        TrainPlace trainPlace;
        try {
            trainPlace = TrainPlace.valueOf(place);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ProblemWithSeatsCount("Unknown place type: " + place);
        }
        if(trainPlace != TrainPlace.COMPARTMENT && trainPlace != TrainPlace.SUITE){
            throw new ProblemWithSeatsCount("Unsupported place type: " + place);
        }
        return trainPlace;
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
        BigDecimal ticketCountValue = BigDecimal.valueOf(parseTicketsCount(ticketCount));
        RouteDTO routeDTO = routeFacade.convertRouteToRouteDTO(routeService.findRouteById(id));
        // default раньше возвращал ZERO, то есть неизвестный тип места давал бесплатный билет.
        BigDecimal placePrice = switch (parsePlace(place)) {
            case COMPARTMENT -> routeDTO.getPriseOfCompartmentTicket();
            case SUITE -> routeDTO.getPriseOfSuiteTicket();
            default -> throw new ProblemWithSeatsCount("Unsupported place type: " + place);
        };
        return priceFactor.multiply(ticketCountValue).multiply(placePrice);
    }

}
