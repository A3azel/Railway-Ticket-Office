package com.example.springbootpetproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketTypeDTO {
    private Long id;
    private String created;
    private String updated;
    private String createdBy;
    private String lastModifiedBy;
    @NotBlank(message = "This field can't be blank")
    private String ticketTypeName;
    @NotNull(message = "This field can't be blank")
    @DecimalMin(value = "0.00", inclusive = false, message = "Price factor can't negative")
    @Digits(integer = 1, fraction = 2, message = "Example 1.23")
    private double ticketPriceFactor;
    private boolean isRelevant;
}
