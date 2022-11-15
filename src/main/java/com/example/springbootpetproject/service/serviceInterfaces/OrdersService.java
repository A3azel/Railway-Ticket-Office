package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.dto.OrderDTO;
import com.example.springbootpetproject.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrdersService {
    void addOrder(Order order);

    Order getOrderById(Long id);

    List<OrderDTO> getAllUserOrders(Long id);

    Page<OrderDTO> getAllUserOrdersByUserName(String username, Pageable pageable, int pageNumber, String direction, String sort);

    boolean exitByUserNameAndTrainName(String userName, String trainName);
    boolean exitByUserNameAndOrderID(String userName, Long id);
}
