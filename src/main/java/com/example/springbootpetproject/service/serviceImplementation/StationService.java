package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.entity.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.springbootpetproject.repository.StationRepository;
import com.example.springbootpetproject.service.serviceInterfaces.StationServiceInterface;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class StationService implements StationServiceInterface {
    private final StationRepository stationRepository;

    @Autowired
    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    @Transactional
    public boolean addStation(Station station) {
        Set<Station> stationSet = new HashSet<>(stationRepository.findAll());
        if(stationSet.contains(station)){
            return false;
        }
        stationRepository.save(station);
        return true;
    }

    @Override
    @Transactional
    public void deleteStation(Long id) {
        stationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Station> getAllStationInCity(String cityName, Pageable pageable, int pageNumber) {
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize());
        return stationRepository.getStationByCity_CityName(cityName,changePageable);
    }

    @Override
    public Station findStationByStationName(String stationName) {
        Station station = stationRepository.findByStationName(stationName);
        if(station!= null){
            return station;
        }
        throw new IllegalArgumentException("Станцію не знайдено");

    }
}
