package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.entity.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.springbootpetproject.repository.StationRepository;
import com.example.springbootpetproject.service.serviceInterfaces.StationServiceInterface;

import java.util.HashSet;
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
    public void addStation(Station station) {
        Set<Station> stationSet = new HashSet<>(stationRepository.findAll());
        if(stationSet.contains(station)){
            return;
        }
        stationRepository.save(station);
    }

    @Override
    @Transactional
    public void deleteStation(Long id) {
        stationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Station> getAllStationInCity(String cityName, Pageable pageable, int pageNumber, String direction, String sort) {
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()
                ,direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        return stationRepository.getStationByCity_CityName(cityName,changePageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Station findStationByStationName(String stationName) {
        Station station = stationRepository.findByStationName(stationName);
        if(station!= null){
            return station;
        }
        throw new IllegalArgumentException("Станцію не знайдено");

    }

    @Override
    @Transactional(readOnly = true)
    public Station findByID(Long id) {
        return stationRepository.findStationById(id);
    }

    @Override
    @Transactional
    public void updateStation(Station station) {
        stationRepository.save(station);
    }
}
