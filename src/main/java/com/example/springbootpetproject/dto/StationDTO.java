package com.example.springbootpetproject.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationDTO {
    private Long id;
    private String stationName;
    private String cityName;
}
