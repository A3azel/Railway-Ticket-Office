package com.example.springbootpetproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainDTO {
    private Long id;
    private String trainNumber;
    private int numberOfCompartmentSeats;
    private int numberOfSuiteSeats;
    private boolean isRelevant;
}
