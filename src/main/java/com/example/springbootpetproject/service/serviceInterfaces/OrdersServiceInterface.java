package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrdersServiceInterface {
    void addOrder(Orders order);

    Orders getOrderById(Long id);

    List<Orders> getAllUserOrders(Long id);

    Page<Orders> getAllUserOrdersByUserName(String username, Pageable pageable, int pageNumber, String direction, String sort);

    boolean exitByUserNameAndTrainName(String userName, String trainName);
}
