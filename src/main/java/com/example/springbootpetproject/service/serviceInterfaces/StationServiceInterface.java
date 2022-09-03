package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.entity.Station;

import java.util.List;

public interface StationServiceInterface {
    boolean addStation(Station station);

    boolean deleteStation(Long id);

    List<Station> getAllStationInCity(String cityName);

    Station findStationByStationName(String stationName);
}
