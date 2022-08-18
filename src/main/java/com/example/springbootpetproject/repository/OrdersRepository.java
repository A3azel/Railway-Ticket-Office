package com.example.springbootpetproject.repository;

import com.example.springbootpetproject.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders,Long> {
    List<Orders> getAllByUser_Id(Long Id);

    Orders getOrdersById(Long Id);


}
