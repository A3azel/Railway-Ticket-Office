package com.example.springbootpetproject.dto;

import com.example.springbootpetproject.entity.NumberOfPurchasedTicketsTypes;
import com.example.springbootpetproject.entity.Route;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private String created;
    private String updated;
    private String createdBy;
    private String lastModifiedBy;
    private BigDecimal orderPrise;
    private String username;
    private String firstName;
    private String lastName;
    private RouteDTO route;
    private Set<NumberOfPurchasedTicketsTypes> numberOfPurchasedTicketsTypesSet;
}
