package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.customExceptions.CityExceptions.CityAlreadyExist;
import com.example.springbootpetproject.dto.CityDTO;
import com.example.springbootpetproject.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CityServiceInterface {
    Page<CityDTO> findAllCity(Pageable pageable, int pageNumber, String direction, String sort);

    void addCity(City city) throws CityAlreadyExist;

    City findByCityName(String cityName);

    boolean cityIsExist(String cityName);

    City findCityById(Long id);

    void setCityRelevant(Long id);

    CityDTO convertCityToCityDTO(City city);
}
