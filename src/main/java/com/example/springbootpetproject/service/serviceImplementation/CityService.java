package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.customExceptions.cityExceptions.CityAlreadyExist;
import com.example.springbootpetproject.customExceptions.cityExceptions.CityNotFound;
import com.example.springbootpetproject.dto.CityDTO;
import com.example.springbootpetproject.entity.City;
import com.example.springbootpetproject.entity.Station;
import com.example.springbootpetproject.repository.CityRepository;
import com.example.springbootpetproject.service.serviceInterfaces.CityServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Format;
import java.text.SimpleDateFormat;

@Service
@Slf4j
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
        log.debug("In the findAllCity method");
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()
                ,direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        Page<City> cityPage = cityRepository.findAll(changePageable);
        log.info("Page<City>: {}", cityPage);
        Page<CityDTO> cityDTOPage = cityPage.map(this::convertCityToCityDTO);
        log.info("Page<City>: {}", cityDTOPage);
        log.debug("End of findAllCity method");
        return cityDTOPage;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void addCity(City city) throws CityAlreadyExist {
        log.debug("In the addCity method");
        if(cityIsExist(city.getCityName())){
            throw new CityAlreadyExist("City with the specified name already exist");
        }
        city.setRelevant(true);
        log.info("Changed the relevant value to true for City object");
        log.debug("End of addCity method");
        cityRepository.save(city);
    }
    // isn't used
    @Override
    @Transactional(readOnly = true)
    public City findCityByCityName(String cityName){
        log.debug("In the findCityByCityName method");
        City wontedCity = cityRepository.findCityByCityName(cityName);
        log.info("City wontedCity: {}", wontedCity.getCityName());
        log.debug("End of findCityByCityName method");
        return wontedCity;
    }

    @Override
    @Transactional(readOnly = true)
    public City findByCityName(String cityName) throws CityNotFound {
        log.debug("In the findByCityName method");
        City wontedCity =  cityRepository.findByCityName(cityName)
                .orElseThrow(()->{
                    CityNotFound cityNotFound = new CityNotFound("City with the specified name not found");
                    log.info(String.format("City with the %1$s name not found",cityName));
                    return cityNotFound;
                });
        log.info("City wontedCity: {}", wontedCity.getCityName());
        log.debug("End of findByCityName method");
        return wontedCity;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean cityIsExist(String cityName) {
        log.debug("In the cityIsExist method");
        boolean isCityExist = cityRepository.existsCityByCityName(cityName);
        log.info("boolean isCityExist: {}", isCityExist);
        log.debug("End of cityIsExist method");
        return isCityExist;
    }

    @Override
    @Transactional(readOnly = true)
    public City findCityById(Long id) {
        log.debug("In the findCityById method");
        City wontedCity = cityRepository.findCityById(id);
        log.info("City wontedCity: {}", wontedCity.getCityName());
        log.debug("End of findCityById method");
        return wontedCity;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void setCityRelevant(Long id) {
        log.debug("In the setCityRelevant method");
        boolean isRelevant = findCityById(id).isRelevant();
        boolean notIsRelevant = !isRelevant;
        City selectCity = findCityById(id);
        log.info("City wontedCity: {}", selectCity.getCityName());
        for (Station station:selectCity.getStationSet()) {
            stationService.setStationRelevantByCity(notIsRelevant,station.getId());
        }
        cityRepository.setCityRelevant(notIsRelevant,id);
        log.info("Set relevant for {}",selectCity.getCityName());
        log.debug("End of setCityRelevant method");
    }

    @Override
    public CityDTO convertCityToCityDTO(City city){
        log.debug("In the convertCityToCityDTO method");
        CityDTO cityDTO = new CityDTO();
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cityDTO.setId(city.getId());
        cityDTO.setCreated(formatter.format(city.getCreated()));
        cityDTO.setUpdated(formatter.format(city.getUpdated()));
        cityDTO.setCreatedBy(city.getCreatedBy());
        cityDTO.setCityName(city.getCityName());
        cityDTO.setRelevant(city.isRelevant());
        log.info("CityDTO converted city: {}", cityDTO);
        log.debug("End of convertCityToCityDTO method");
        return cityDTO;
    }
}
