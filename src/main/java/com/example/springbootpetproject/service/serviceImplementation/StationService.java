package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.customExceptions.StationExceptions.StationAlreadyExist;
import com.example.springbootpetproject.dto.StationDTO;
import com.example.springbootpetproject.entity.Station;
import com.example.springbootpetproject.repository.CityRepository;
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

@Service
public class StationService implements StationServiceInterface {
    private final StationRepository stationRepository;
    //private final CityService cityService;
    private final CityRepository cityRepository;

    @Autowired
    public StationService(StationRepository stationRepository, CityRepository cityRepository) {
        this.stationRepository = stationRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void addStation(StationDTO stationDTO) throws StationAlreadyExist {
        if(existStationByStationNameAndCity(stationDTO.getStationName(),stationDTO.getCityName())){
            throw new StationAlreadyExist("Station with the specified name in this city already exist");
        }
        Station station = new Station();
        station.setStationName(stationDTO.getStationName());
        station.setCity(cityRepository.findByCityName(stationDTO.getCityName()));
        //station.setCity(cityService.findByCityName(stationDTO.getCityName()));
        station.setRelevant(true);
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
    @Transactional(readOnly = true)
    public boolean existStationByStationNameAndCity(String stationName,String cityName) {
        return stationRepository.existsByStationNameAndCity_CityName(stationName,cityName);
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
