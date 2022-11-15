package com.example.springbootpetproject.service.serviceImplementation;


import com.example.springbootpetproject.dto.OrderDTO;
import com.example.springbootpetproject.entity.Order;
import com.example.springbootpetproject.facade.OrderFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.springbootpetproject.repository.OrdersRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersServiceI implements com.example.springbootpetproject.service.serviceInterfaces.OrdersService {
    private final OrdersRepository ordersRepository;
    private final OrderFacade orderFacade;


    @Autowired
    public OrdersServiceI(OrdersRepository ordersRepository, OrderFacade orderFacade) {
        this.ordersRepository = ordersRepository;
        this.orderFacade = orderFacade;
    }

    @Override
    @Transactional
    public void addOrder(Order order) {
        //trainRepository.reduceTheNumberOfSeats(order.getTrain().getTrainNumber(),order.getCountOfPurchasedTickets());
        //ordersRepository.save(order);
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

}
