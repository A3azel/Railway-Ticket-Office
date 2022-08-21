package com.example.springbootpetproject.service.serviceInterfaces;

import com.example.springbootpetproject.entity.City;

import java.util.List;

public interface CityServiceInterface {
    List<City> getAllCity();

    void addCity(City city);
}
