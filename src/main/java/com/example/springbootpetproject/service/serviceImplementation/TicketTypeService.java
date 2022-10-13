package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.customExceptions.ticketExeptions.TicketAlreadyExist;
import com.example.springbootpetproject.customExceptions.ticketExeptions.TicketNotFound;
import com.example.springbootpetproject.dto.TicketTypeDTO;
import com.example.springbootpetproject.entity.TicketType;
import com.example.springbootpetproject.repository.TicketTypeRepository;
import com.example.springbootpetproject.service.serviceInterfaces.TicketTypeServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
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
    @PreAuthorize("hasRole('ADMIN')")
    public void addTicketType(TicketType ticketType) throws TicketAlreadyExist {
        if(existTicketByName(ticketType.getTicketTypeName())){
            throw new TicketAlreadyExist("Ticket with the specified name already exist");
        }
        ticketType.setRelevant(true);
        ticketTypeRepository.save(ticketType);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void updateTicketInfo(TicketType ticket, Long id) throws TicketAlreadyExist {
        if(existTicketByName(ticket.getTicketTypeName())){
            throw new TicketAlreadyExist("Ticket with the specified name already exist");
        }
        TicketType selectedTicket = getTicketById(id);
        selectedTicket.setTicketTypeName(ticket.getTicketTypeName());
        selectedTicket.setTicketPriceFactor(ticket.getTicketPriceFactor());
        ticketTypeRepository.save(selectedTicket);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public boolean deleteTicketTypeByTicketTypeName(String ticketTypeName) {
        ticketTypeRepository.deleteTicketTypeByTicketTypeName(ticketTypeName);
        return false;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
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
    @Transactional(readOnly = true)
    public Page<TicketTypeDTO> findAllByRelevantTrue(Pageable pageable, int pageNumber, String direction, String sort) {
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()
                ,direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        Page<TicketType> ticketTypes = ticketTypeRepository.findAll(changePageable);
        Page<TicketTypeDTO> ticketTypeDTOPage = ticketTypes.map(this::convertTicketTypeToTicketTypeDTO);
        return ticketTypeDTOPage;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketTypeDTO> getAllTicketTypesForOrder() {
        List<TicketType> ticketTypeList = ticketTypeRepository.findAll();
        return ticketTypeList
                .stream()
                .map(this::convertTicketTypeToTicketTypeDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TicketType getTicketById(Long id) {
        return ticketTypeRepository.findTicketTypeById(id);
    }

    @Override
    public TicketType getTicketByTicketType(String ticketType) {
        return ticketTypeRepository.findTicketTypeByTicketTypeName(ticketType);
    }

    @Override
    public TicketType findByTicketName(String ticketName) throws TicketNotFound {
        return ticketTypeRepository.findByTicketTypeName(ticketName)
                .orElseThrow(()->new TicketNotFound("Ticket with specified name was not found"));
    }

    @Override
    public boolean existTicketByName(String ticketName) {
        return ticketTypeRepository.existsTicketTypeByTicketTypeName(ticketName);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTicketById(Long id) {
        ticketTypeRepository.deleteById(id);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void setTicketRelevant(Long id) {
        boolean isRelevant = getTicketById(id).isRelevant();
        boolean notIsRelevant = !isRelevant;
        ticketTypeRepository.setTicketRelevant(notIsRelevant,id);
    }

    @Override
    public TicketTypeDTO convertTicketTypeToTicketTypeDTO(TicketType ticketType){
        TicketTypeDTO ticketTypeDTO = new TicketTypeDTO();
        ticketTypeDTO.setId(ticketType.getId());
        ticketTypeDTO.setTicketTypeName(ticketType.getTicketTypeName());
        ticketTypeDTO.setTicketPriceFactor(ticketType.getTicketPriceFactor());
        ticketTypeDTO.setRelevant(ticketType.isRelevant());
        return ticketTypeDTO;
    }


}
