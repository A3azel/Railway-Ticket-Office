package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.dto.StationDTO;
import com.example.springbootpetproject.entity.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StationServiceInterface {
    void addStation(Station station);

    void deleteStation(Long id);

    Page<StationDTO> getAllStationInCity(String cityName, Pageable pageable, int pageNumber, String direction, String sort);

    Station findStationByStationName(String stationName);

    Station findByID(Long id);

    void updateStation(Station station);

    void setStationRelevant(Long id);

    void setStationRelevantByCity(boolean relevant, Long id);

    StationDTO convertStationToStationDTO(Station station);


}
