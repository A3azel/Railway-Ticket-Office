package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.customExceptions.CityExceptions.CityAlreadyExist;
import com.example.springbootpetproject.dto.CityDTO;
import com.example.springbootpetproject.entity.City;
import com.example.springbootpetproject.entity.Station;
import com.example.springbootpetproject.repository.CityRepository;
import com.example.springbootpetproject.service.serviceInterfaces.CityServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CityService implements CityServiceInterface {
    private final CityRepository cityRepository;
    private final StationService stationService;

    @Autowired
    public CityService(CityRepository cityRepository, StationService stationService) {
        this.cityRepository = cityRepository;
        this.stationService = stationService;
    }


    @Override
    @Transactional(readOnly = true)
    public Page<CityDTO> findAllCity(Pageable pageable, int pageNumber, String direction, String sort) {
        //Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()).withSort(Sort.by(direction,sort));
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()
                ,direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        Page<City> cityPage = cityRepository.findAll(changePageable);
        Page<CityDTO> cityDTOPage = cityPage.map(this::convertCityToCityDTO);
        return cityDTOPage;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void addCity(City city) throws CityAlreadyExist {
        if(cityIsExist(city.getCityName())){
            throw new CityAlreadyExist("City with the specified name already exist");
        }
        city.setRelevant(true);
        cityRepository.save(city);
    }

    @Override
    @Transactional(readOnly = true)
    public City findByCityName(String cityName) {
        return cityRepository.findByCityName(cityName);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean cityIsExist(String cityName) {
        return cityRepository.existsCityByCityName(cityName);
    }

    @Override
    @Transactional(readOnly = true)
    public City findCityById(Long id) {
        return cityRepository.findCityById(id);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void setCityRelevant(Long id) {
        boolean isRelevant = findCityById(id).isRelevant();
        boolean notIsRelevant = !isRelevant;
        City selectCity = findCityById(id);
        for (Station station:selectCity.getStationSet()) {
            stationService.setStationRelevantByCity(notIsRelevant,station.getId());
        }
        cityRepository.setCityRelevant(notIsRelevant,id);
    }

    @Override
    public CityDTO convertCityToCityDTO(City city){
        CityDTO cityDTO = new CityDTO();
        cityDTO.setId(city.getId());
        cityDTO.setCityName(city.getCityName());
        cityDTO.setRelevant(city.isRelevant());
        return cityDTO;
    }
}
