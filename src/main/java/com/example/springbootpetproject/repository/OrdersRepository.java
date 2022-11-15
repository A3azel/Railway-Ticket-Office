package com.example.springbootpetproject.repository;

import com.example.springbootpetproject.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Order,Long> {
    List<Order> getAllByUser_Id(Long Id);

    Page<Order> getAllByUser_username(String username, Pageable pageable);

    Order getOrdersById(Long Id);

    boolean existsOrdersByUser_usernameAndRoute_Train_TrainNumber(String username, String trainNumber);

    boolean existsOrdersByUser_usernameAndId(String username, Long orderID);
}
