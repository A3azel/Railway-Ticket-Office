package com.example.springbootpetproject.repository;

import com.example.springbootpetproject.entity.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station,Long> {
    Page<Station> getStationByCity_CityName(String cityName,Pageable pageable);

    Station findByStationName(String stationName);

    void deleteById(Long id);

    Station findStationById(Long id);
}
