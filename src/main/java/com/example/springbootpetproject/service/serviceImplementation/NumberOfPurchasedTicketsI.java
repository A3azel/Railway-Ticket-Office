package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.entity.NumberOfPurchasedTicketsTypes;
import com.example.springbootpetproject.repository.NumberOfPurchasedTicketsTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NumberOfPurchasedTicketsI implements com.example.springbootpetproject.service.serviceInterfaces.NumberOfPurchasedTicketsService {
    private final NumberOfPurchasedTicketsTypesRepository number;

    @Autowired
    public NumberOfPurchasedTicketsI(NumberOfPurchasedTicketsTypesRepository number) {
        this.number = number;
    }


    @Override
    @Transactional
    public void addNumberOfPurchasedTickets(NumberOfPurchasedTicketsTypes ticketsNumber) {
        number.save(ticketsNumber);
    }
}
