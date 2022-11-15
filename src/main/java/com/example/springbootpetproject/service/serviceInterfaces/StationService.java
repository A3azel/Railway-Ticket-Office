package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.customExceptions.cityExceptions.CityNotFound;
import com.example.springbootpetproject.customExceptions.stationExceptions.StationAlreadyExist;
import com.example.springbootpetproject.customExceptions.stationExceptions.StationNotFound;
import com.example.springbootpetproject.dto.StationDTO;
import com.example.springbootpetproject.entity.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StationService {
    void addStation(StationDTO stationDTO) throws StationAlreadyExist;

    void deleteStation(Long id);

    Page<StationDTO> getAllStationInCity(String cityName, Pageable pageable, int pageNumber, String direction, String sort);

    Station findStationByStationName(String stationName);

    Station findByID(Long id);

    Station findByStationNameAndCityName(String stationName,String cityName) throws StationNotFound;

    boolean existStationByStationNameAndCity(String stationName,String cityName);

    void updateStation(StationDTO stationDTO, Long id) throws StationAlreadyExist, CityNotFound;

    void setStationRelevant(Long id);

    void setStationRelevantByCity(boolean relevant, Long id);

}
