package com.example.springbootpetproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketTypeDTO {
    private Long id;
    private String ticketTypeName;
    private double ticketPriceFactor;
    private boolean isRelevant;
}
