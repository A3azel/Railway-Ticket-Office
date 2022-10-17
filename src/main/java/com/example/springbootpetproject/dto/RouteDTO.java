package com.example.springbootpetproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteDTO {
    private Long id;
    private String created;
    private String updated;
    private String createdBy;
    private String lastModifiedBy;
    @NotBlank(message = "This field can't be blank")
    private String startStationName;
    @NotBlank(message = "This field can't be blank")
    private String startCityName;
    private LocalDateTime departureTime;
    private LocalTime travelTime;
    @NotBlank(message = "This field can't be blank")
    private String arrivalStationName;
    @NotBlank(message = "This field can't be blank")
    private String arrivalCityName;
    private LocalDateTime arrivalTime;
   /* private LocalDate dateOfDispatch;
    private LocalTime timeOfDispatch;
    private LocalDate dateOfArrival;
    private LocalTime timeOfArrival;*/
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Min(value = 0 , message = "Count of compartment free seats can't negative")
    private int numberOfCompartmentFreeSeats;
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Min(value = 0 , message = "Count of compartment free seats can't negative")
    private int numberOfSuiteFreeSeats;
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Min(value = 0 , message = "Count of compartment free seats can't negative")
    private BigDecimal priseOfCompartmentTicket;
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Min(value = 0 , message = "Count of compartment free seats can't negative")
    private BigDecimal priseOfSuiteTicket;
    @NotBlank(message = "This field can't be blank")
    private String trainNumber;
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Min(value = 0 , message = "Count of compartment free seats can't negative")
    private int numberOfCompartmentSeats;
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Min(value = 0 , message = "Count of compartment free seats can't negative")
    private int numberOfSuiteSeats;

}
