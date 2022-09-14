package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.dto.TicketTypeDTO;
import com.example.springbootpetproject.entity.TicketType;
import com.example.springbootpetproject.repository.TicketTypeRepository;
import com.example.springbootpetproject.service.serviceInterfaces.TicketTypeServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketTypeService implements TicketTypeServiceInterface {
    private final TicketTypeRepository ticketTypeRepository;

    @Autowired
    public TicketTypeService(TicketTypeRepository ticketTypeRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
    }

    @Override
    @Transactional
    public boolean addTicketType(TicketType ticketType) {
        ticketTypeRepository.save(ticketType);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteTicketTypeByTicketTypeName(String ticketTypeName) {
        ticketTypeRepository.deleteTicketTypeByTicketType(ticketTypeName);
        return false;
    }

    @Override
    @Transactional
    public boolean setTicketPriceFactor(String ticketTypeName, float priceFactor) {
        ticketTypeRepository.setTicketPriceFactor(ticketTypeName,priceFactor);
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketType> getAllTicketTypes() {
        return ticketTypeRepository.findAll();
    }

    @Override
    public TicketTypeDTO convertTicketTypeToTicketTypeDTO(TicketType ticketType){
        TicketTypeDTO ticketTypeDTO = new TicketTypeDTO();
        ticketTypeDTO.setId(ticketType.getId());
        ticketTypeDTO.setTicketType(ticketType.getTicketType());
        ticketTypeDTO.setTicketPriceFactor(ticketType.getTicketPriceFactor());
        return ticketTypeDTO;
    }
}
