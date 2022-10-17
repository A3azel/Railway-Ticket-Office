package com.example.springbootpetproject.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationDTO {
    private Long id;
    private String created;
    private String updated;
    private String createdBy;
    private String lastModifiedBy;
    @NotBlank(message = "This field can't be blank")
    private String stationName;
    @NotBlank(message = "This field can't be blank")
    private String cityName;
    private boolean isRelevant;
}
