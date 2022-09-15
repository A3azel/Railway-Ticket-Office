package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.dto.StationDTO;
import com.example.springbootpetproject.entity.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StationServiceInterface {
    void addStation(Station station);

    void deleteStation(Long id);

    Page<StationDTO> getAllStationInCity(String cityName, Pageable pageable, int pageNumber, String direction, String sort);

    Station findStationByStationName(String stationName);

    Station findByID(Long id);

    void updateStation(Station station);

    StationDTO convertStationToStationDTO(Station station);
}
