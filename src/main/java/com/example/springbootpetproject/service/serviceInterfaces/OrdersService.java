package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.customExceptions.userExceptions.InsufficientFunds;
import com.example.springbootpetproject.dto.OrderDTO;
import com.example.springbootpetproject.entity.NumberOfPurchasedTicketsTypes;
import com.example.springbootpetproject.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface OrdersService {
    void addOrder(String username, String ticketCount, String place, String ticketType, Long routeID) throws InsufficientFunds;
    void addMultipleOrder(String username, List<NumberOfPurchasedTicketsTypes> ticketsTypesList,
             String ticketCount, String place, String ticketType, Long routeID) throws InsufficientFunds;

    Order getOrderById(Long id);

    List<OrderDTO> getAllUserOrders(Long id);

    Page<OrderDTO> getAllUserOrdersByUserName(String username, Pageable pageable, int pageNumber, String direction, String sort);

    boolean exitByUserNameAndTrainName(String userName, String trainName);
    boolean exitByUserNameAndOrderID(String userName, Long id);
    BigDecimal calculateAllPrice(Long id, String place, String ticketType, String ticketCount);
}
