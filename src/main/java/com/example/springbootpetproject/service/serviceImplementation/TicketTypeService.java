package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.dto.TicketTypeDTO;
import com.example.springbootpetproject.entity.TicketType;
import com.example.springbootpetproject.repository.TicketTypeRepository;
import com.example.springbootpetproject.service.serviceInterfaces.TicketTypeServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public Page<TicketTypeDTO> getAllTicketTypes(Pageable pageable, int pageNumber, String direction, String sort) {
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()
                ,direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        Page<TicketType> ticketTypes = ticketTypeRepository.findAll(changePageable);
        Page<TicketTypeDTO> ticketTypeDTOPage = ticketTypes.map(this::convertTicketTypeToTicketTypeDTO);
        return ticketTypeDTOPage;
    }

    @Override
    public List<TicketTypeDTO> getAllTicketTypesForOrder() {
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        return ticketTypeList
                .stream()
                .map(this::convertTicketTypeToTicketTypeDTO)
                .collect(Collectors.toList());
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
