package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.dto.CityDTO;
import com.example.springbootpetproject.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CityServiceInterface {
    Page<City> findAllCity(Pageable pageable, int pageNumber, String direction, String sort);

    void addCity(City city);

    City findByCityName(String cityName);

    boolean cityIsExist(String cityName);

    CityDTO convertCityToCityDTO(City city);
}
