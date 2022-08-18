package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.entity.Orders;

import java.util.List;

public interface OrdersServiceInterface {
    void addOrder(Orders order);

    Orders getOrderById(Long id);

    List<Orders> getAllUserOrders(Long id);

}
