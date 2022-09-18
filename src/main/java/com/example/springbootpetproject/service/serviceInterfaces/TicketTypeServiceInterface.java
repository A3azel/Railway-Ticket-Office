package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.dto.TicketTypeDTO;
import com.example.springbootpetproject.entity.TicketType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TicketTypeServiceInterface {
    boolean addTicketType(TicketType ticketType);

    boolean deleteTicketTypeByTicketTypeName(String ticketTypeName);

    boolean setTicketPriceFactor(String ticketTypeName, float priceFactor);

    Page<TicketTypeDTO> getAllTicketTypes(Pageable pageable, int pageNumber, String direction, String sort);

    List<TicketTypeDTO> getAllTicketTypesForOrder();

    TicketType getTicketById(Long id);

    void deleteTicketById(Long id);

    TicketTypeDTO convertTicketTypeToTicketTypeDTO(TicketType ticketType);
}
