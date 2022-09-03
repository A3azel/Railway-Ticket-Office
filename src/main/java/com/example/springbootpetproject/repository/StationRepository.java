package com.example.springbootpetproject.repository;

import com.example.springbootpetproject.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station,Long> {
    List<Station> getStationByCity_CityName(String cityName);

    Station findByStationName(String stationName);
}
