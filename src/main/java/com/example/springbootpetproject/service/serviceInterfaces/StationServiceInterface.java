package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.customExceptions.CityExceptions.CityNotFound;
import com.example.springbootpetproject.customExceptions.StationExceptions.StationAlreadyExist;
import com.example.springbootpetproject.dto.StationDTO;
import com.example.springbootpetproject.entity.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StationServiceInterface {
    void addStation(StationDTO stationDTO) throws StationAlreadyExist;

    void deleteStation(Long id);

    Page<StationDTO> getAllStationInCity(String cityName, Pageable pageable, int pageNumber, String direction, String sort);

    Station findStationByStationName(String stationName);

    Station findByID(Long id);

    boolean existStationByStationNameAndCity(String stationName,String cityName);

    void updateStation(StationDTO stationDTO, Long id) throws StationAlreadyExist, CityNotFound;

    void setStationRelevant(Long id);

    void setStationRelevantByCity(boolean relevant, Long id);

    StationDTO convertStationToStationDTO(Station station);


}
