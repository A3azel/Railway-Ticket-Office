package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.entity.City;
import com.example.springbootpetproject.repository.CityRepository;
import com.example.springbootpetproject.service.serviceInterfaces.CityServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CityService implements CityServiceInterface {
    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<City> getAllCity() {
        return cityRepository.findAll();
    }

    @Override
    @Transactional
    public void addCity(City city) {
        cityRepository.save(city);
    }
}
