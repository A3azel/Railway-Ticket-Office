package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.entity.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.springbootpetproject.repository.StationRepository;
import com.example.springbootpetproject.service.serviceInterfaces.StationServiceInterface;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class StationService implements StationServiceInterface {
    public final StationRepository stationRepository;

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
    public boolean deleteStation(Long id) {
        stationRepository.deleteById(id);
        return false;
    }

    @Override
    public List<Station> getAllStationInCity(String cityName) {
        return stationRepository.getStationByCity_CityName(cityName);
    }
}
