package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.entity.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StationServiceInterface {
    boolean addStation(Station station);

    void deleteStation(Long id);

    Page<Station> getAllStationInCity(String cityName, Pageable pageable, int pageNumber);

    Station findStationByStationName(String stationName);
}
