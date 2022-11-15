package com.example.springbootpetproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
    private String trainNumber;

    @NotBlank(message = "This field can't be blank")
    private String startStationName;

    @NotBlank(message = "This field can't be blank")
    private String startCityName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime departureTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime travelTime;

    @NotBlank(message = "This field can't be blank")
    private String arrivalStationName;

    @NotBlank(message = "This field can't be blank")
    private String arrivalCityName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime arrivalTime;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Min(value = 0 , message = "Count of compartment free seats can't negative")
    private int numberOfCompartmentFreeSeats;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Min(value = 0 , message = "Count of suite free seats can't negative")
    @Max(value = 9999, message = "The number of free seats in the suite cannot exceed 9999")
    private int numberOfSuiteFreeSeats;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @DecimalMin(value = "0.00", message = "Price can't negative")
    private BigDecimal priseOfCompartmentTicket;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @DecimalMin(value = "0.00", message = "Price can't negative")
    private BigDecimal priseOfSuiteTicket;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Min(value = 0 , message = "Count of compartment free seats can't negative")
    @Max(value = 9999, message = "The number of free seats in the compartment cannot exceed 9999")
    private int numberOfCompartmentSeats;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Min(value = 0 , message = "Count of compartment free seats can't negative")
    private int numberOfSuiteSeats;

    private List<String> intermediateStations;

}
