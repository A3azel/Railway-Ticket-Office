package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.dto.TicketTypeDTO;
import com.example.springbootpetproject.entity.TicketType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface TicketTypeServiceInterface {
    void addTicketType(TicketType ticketType);

    void updateTicketInfo(Map<String,String> allParam);

    boolean deleteTicketTypeByTicketTypeName(String ticketTypeName);

    boolean setTicketPriceFactor(String ticketTypeName, float priceFactor);

    Page<TicketTypeDTO> getAllTicketTypes(Pageable pageable, int pageNumber, String direction, String sort);

    Page<TicketTypeDTO> findAllByRelevantTrue(Pageable pageable, int pageNumber, String direction, String sort);

    List<TicketTypeDTO> getAllTicketTypesForOrder();

    TicketType getTicketById(Long id);

    TicketType getTicketByTicketType(String ticketTypeName);

    void deleteTicketById(Long id);

    void setTicketRelevant(Long id);

    TicketTypeDTO convertTicketTypeToTicketTypeDTO(TicketType ticketTypeName);


}
