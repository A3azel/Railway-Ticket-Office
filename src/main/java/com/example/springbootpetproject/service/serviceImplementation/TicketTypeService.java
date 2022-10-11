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

   /* @Override
    @Transactional
    public void addTicketType(Map<String,String> allParam) {
        TicketType ticketType = new TicketType();
        ticketType.setTicketTypeName(allParam.get("ticketType"));
        double ticketPriceFactor = Double.parseDouble(allParam.get("ticketPriceFactor"));
        String formattedDouble = new DecimalFormat("#0.00").format(ticketPriceFactor);
        formattedDouble = formattedDouble.replaceAll(",",".");
        double finalTicketPriceFactor = Double.parseDouble(formattedDouble);
        ticketType.setTicketPriceFactor(finalTicketPriceFactor);
        ticketType.setRelevant(true);
        ticketTypeRepository.save(ticketType);
    }*/

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void addTicketType(TicketType ticketType) {
        ticketType.setRelevant(true);
        ticketTypeRepository.save(ticketType);
    }



    /*@Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void updateTicketInfo(Map<String, String> allParam) {
        TicketType ticketType = getTicketById(Long.valueOf(allParam.get("id")));
        ticketType.setTicketTypeName(allParam.get("ticketType"));
        double ticketPriceFactor = Double.parseDouble(allParam.get("ticketPriceFactor"));
        String formattedDouble = new DecimalFormat("#0.00").format(ticketPriceFactor);
        formattedDouble = formattedDouble.replaceAll(",",".");
        double finalTicketPriceFactor = Double.parseDouble(formattedDouble);
        ticketType.setTicketPriceFactor(finalTicketPriceFactor);
        ticketTypeRepository.save(ticketType);
    }*/

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void updateTicketInfo(TicketType ticket, Long id) {
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
