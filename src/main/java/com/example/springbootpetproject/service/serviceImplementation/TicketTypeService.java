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
    public void addTicketType(Map<String,String> allParam) {
        TicketType ticketType = new TicketType();
        ticketType.setTicketType(allParam.get("ticketName"));
        double ticketPriceFactor = Double.parseDouble(allParam.get("ticketPriceFactor"));
        String formattedDouble = new DecimalFormat("#0.00").format(ticketPriceFactor);
        formattedDouble = formattedDouble.replaceAll(",",".");
        double finalTicketPriceFactor = Double.parseDouble(formattedDouble);
        ticketType.setTicketPriceFactor(finalTicketPriceFactor);
        ticketTypeRepository.save(ticketType);
    }

    @Override
    @Transactional
    public void updateTicketInfo(Map<String, String> allParam) {
        TicketType ticketType = getTicketById(Long.valueOf(allParam.get("id")));
        ticketType.setTicketType(allParam.get("ticketType"));
        double ticketPriceFactor = Double.parseDouble(allParam.get("ticketPriceFactor"));
        String formattedDouble = new DecimalFormat("#0.00").format(ticketPriceFactor);
        formattedDouble = formattedDouble.replaceAll(",",".");
        double finalTicketPriceFactor = Double.parseDouble(formattedDouble);
        ticketType.setTicketPriceFactor(finalTicketPriceFactor);
        ticketTypeRepository.save(ticketType);
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
        return ticketTypeRepository.findTicketTypeByTicketType(ticketType);
    }

    @Override
    @Transactional
    public void deleteTicketById(Long id) {
        ticketTypeRepository.deleteById(id);
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
