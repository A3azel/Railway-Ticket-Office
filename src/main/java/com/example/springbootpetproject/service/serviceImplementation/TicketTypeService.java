package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.entity.TicketType;
import com.example.springbootpetproject.repository.TicketTypeRepository;
import com.example.springbootpetproject.service.serviceInterfaces.TicketTypeServiceInterface;

import java.util.List;

public class TicketTypeService implements TicketTypeServiceInterface {
    private final TicketTypeRepository ticketTypeRepository;

    public TicketTypeService(TicketTypeRepository ticketTypeRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
    }

    @Override
    public boolean addTicketType(TicketType ticketType) {
        ticketTypeRepository.save(ticketType);
        return true;
    }

    @Override
    public boolean deleteTicketTypeByTicketTypeName(String ticketTypeName) {
        ticketTypeRepository.deleteTicketTypeByTicketType(ticketTypeName);
        return false;
    }

    @Override
    public boolean setTicketPriceFactor(String ticketTypeName, float priceFactor) {
        ticketTypeRepository.setTicketPriceFactor(ticketTypeName,priceFactor);
        return false;
    }

    @Override
    public List<TicketType> getAllTicketTypes() {
        return ticketTypeRepository.findAll();
    }
}
