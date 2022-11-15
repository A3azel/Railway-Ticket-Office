package com.example.springbootpetproject.facade;

import com.example.springbootpetproject.dto.OrderDTO;
import com.example.springbootpetproject.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.Format;
import java.text.SimpleDateFormat;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderFacade {
    private final RouteFacade routeFacade;

    public OrderDTO convertOrdersToOrdersDTO(Order order){
        OrderDTO orderDTO = new OrderDTO();
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        orderDTO.setId(order.getId());
        orderDTO.setCreated(formatter.format(order.getCreated()));
        orderDTO.setUpdated(formatter.format(order.getUpdated()));
        orderDTO.setCreatedBy(order.getCreatedBy());
        orderDTO.setLastModifiedBy(order.getLastModifiedBy());
        orderDTO.setUsername(order.getUser().getUsername());
        orderDTO.setFirstName(order.getUser().getFirstName());
        orderDTO.setLastName(order.getUser().getLastName());
        orderDTO.setOrderPrise(order.getOrderPrise());
        orderDTO.setRoute(routeFacade.convertRouteToRouteDTO(order.getRoute()));
        orderDTO.setNumberOfPurchasedTicketsTypesSet(order.getNumberOfPurchasedTicketsTypesSet());
        return orderDTO;
    }
}
