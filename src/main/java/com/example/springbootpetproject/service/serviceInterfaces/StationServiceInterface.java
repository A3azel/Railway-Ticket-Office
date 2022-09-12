package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.entity.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StationServiceInterface {
    void addStation(Station station);

    void deleteStation(Long id);

    Page<Station> getAllStationInCity(String cityName, Pageable pageable, int pageNumber, String direction, String sort);

    Station findStationByStationName(String stationName);

    Station findByID(Long id);

    void updateStation(Station station);
}
