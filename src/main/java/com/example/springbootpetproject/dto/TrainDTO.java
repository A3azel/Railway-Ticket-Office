package com.example.springbootpetproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainDTO {
    private Long id;
    private String created;
    private String updated;
    private String createdBy;
    private String lastModifiedBy;
    @NotBlank(message = "This field can't be blank")
    private String trainNumber;
    @Min(value = 0, message = "Count of suite seats can't negative")
    private int numberOfCompartmentSeats;
    @Min(value = 0, message = "Count of suite seats can't negative")
    private int numberOfSuiteSeats;
    private boolean isRelevant;
}
