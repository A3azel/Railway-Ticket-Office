package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.dto.TicketTypeDTO;
import com.example.springbootpetproject.entity.TicketType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface TicketTypeServiceInterface {
    void addTicketType(Map<String,String> allParam);

    void updateTicketInfo(Map<String,String> allParam);

    boolean deleteTicketTypeByTicketTypeName(String ticketTypeName);

    boolean setTicketPriceFactor(String ticketTypeName, float priceFactor);

    Page<TicketTypeDTO> getAllTicketTypes(Pageable pageable, int pageNumber, String direction, String sort);

    List<TicketTypeDTO> getAllTicketTypesForOrder();

    TicketType getTicketById(Long id);

    TicketType getTicketByTicketType(String ticketType);

    void deleteTicketById(Long id);

    TicketTypeDTO convertTicketTypeToTicketTypeDTO(TicketType ticketType);
}
