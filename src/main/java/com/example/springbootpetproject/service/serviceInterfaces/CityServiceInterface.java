package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CityServiceInterface {
    Page<City> findAllCity(Pageable pageable, int pageNumber);

    void addCity(City city);
}
