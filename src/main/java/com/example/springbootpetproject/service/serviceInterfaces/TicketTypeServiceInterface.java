package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.entity.TicketType;

import java.util.List;

public interface TicketTypeServiceInterface {
    boolean addTicketType(TicketType ticketType);

    boolean deleteTicketTypeByTicketTypeName(String ticketTypeName);

    boolean setTicketPriceFactor(String ticketTypeName, float priceFactor);

    List<TicketType> getAllTicketTypes();
}
