package com.example.springbootpetproject.service.serviceImplementation;


import com.example.springbootpetproject.dto.OrdersDTO;
import com.example.springbootpetproject.entity.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.springbootpetproject.repository.OrdersRepository;
import com.example.springbootpetproject.repository.TrainRepository;
import com.example.springbootpetproject.service.serviceInterfaces.OrdersServiceInterface;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersService implements OrdersServiceInterface {
    private final OrdersRepository ordersRepository;
    @Autowired
    public TrainRepository trainRepository;

    @Autowired
    public OrdersService(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @Override
    @Transactional
    public void addOrder(Orders order) {
        //trainRepository.reduceTheNumberOfSeats(order.getTrain().getTrainNumber(),order.getCountOfPurchasedTickets());
        //ordersRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Orders getOrderById(Long id) {
        return ordersRepository.getOrdersById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdersDTO> getAllUserOrders(Long id) {
        return ordersRepository.getAllByUser_Id(id)
                .stream()
                .map(this::convertOrdersToOrdersDTO)
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
    public Page<OrdersDTO> getAllUserOrdersByUserName(String username, Pageable pageable, int pageNumber, String direction, String sort) {
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()
                ,direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        return ordersRepository.getAllByUser_username(username, changePageable)
                .map(this::convertOrdersToOrdersDTO);
    }

    @Override
    public boolean exitByUserNameAndTrainName(String userName, String trainName) {
        return ordersRepository.existsOrdersByUser_usernameAndRoute_Train_TrainNumber(userName, trainName);
    }

    public OrdersDTO convertOrdersToOrdersDTO(Orders orders){
        OrdersDTO ordersDTO = new OrdersDTO();
        return ordersDTO;
    }
}
