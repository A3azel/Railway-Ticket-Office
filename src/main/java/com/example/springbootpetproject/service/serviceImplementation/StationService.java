package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.dto.StationDTO;
import com.example.springbootpetproject.entity.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public void addStation(Station station) {
        Set<Station> stationSet = new HashSet<>(stationRepository.findAll());
        if(stationSet.contains(station)){
            return;
        }
        //station.setRelevant(true);
        stationRepository.save(station);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteStation(Long id) {
        stationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StationDTO> getAllStationInCity(String cityName, Pageable pageable, int pageNumber, String direction, String sort) {
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()
                ,direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        return stationRepository
                .getStationByCity_CityName(cityName,changePageable)
                .map(this::convertStationToStationDTO);
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
    @PreAuthorize("hasRole('ADMIN')")
    public void updateStation(Station station) {
        stationRepository.save(station);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void setStationRelevant(Long id) {
        boolean isRelevant = findByID(id).isRelevant();
        boolean notIsRelevant = !isRelevant;
        stationRepository.setStationRelevant(notIsRelevant,id);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void setStationRelevantByCity(boolean relevant, Long id) {
        stationRepository.setStationRelevantByCity(relevant,id);
    }

    @Override
    public StationDTO convertStationToStationDTO(Station station){
        StationDTO stationDTO = new StationDTO();
        stationDTO.setId(station.getId());
        stationDTO.setStationName(station.getStationName());
        stationDTO.setCityName(station.getCity().getCityName());
        stationDTO.setRelevant(station.isRelevant());
        return stationDTO;
    }
}
