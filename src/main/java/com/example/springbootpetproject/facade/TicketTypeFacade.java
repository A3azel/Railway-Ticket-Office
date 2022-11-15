package com.example.springbootpetproject.facade;

import com.example.springbootpetproject.dto.TicketTypeDTO;
import com.example.springbootpetproject.entity.TicketType;
import org.springframework.stereotype.Component;

import java.text.Format;
import java.text.SimpleDateFormat;

@Component
public class TicketTypeFacade {

    public TicketTypeDTO convertTicketTypeToTicketTypeDTO(TicketType ticketType){
        TicketTypeDTO ticketTypeDTO = new TicketTypeDTO();
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ticketTypeDTO.setId(ticketType.getId());
        ticketTypeDTO.setCreated(formatter.format(ticketType.getCreated()));
        ticketTypeDTO.setUpdated(formatter.format(ticketType.getUpdated()));
        ticketTypeDTO.setCreatedBy(ticketType.getCreatedBy());
        ticketTypeDTO.setLastModifiedBy(ticketType.getLastModifiedBy());
        ticketTypeDTO.setTicketTypeName(ticketType.getTicketTypeName());
        ticketTypeDTO.setTicketPriceFactor(ticketType.getTicketPriceFactor());
        ticketTypeDTO.setRelevant(ticketType.isRelevant());
        return ticketTypeDTO;
    }
}
