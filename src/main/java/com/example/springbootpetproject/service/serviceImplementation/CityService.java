package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.entity.City;
import com.example.springbootpetproject.repository.CityRepository;
import com.example.springbootpetproject.service.serviceInterfaces.CityServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService implements CityServiceInterface {
    public final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public List<City> getAllCity() {
        return cityRepository.findAll();
    }

    @Override
    public void addCity(City city) {
        cityRepository.save(city);
    }
}
