package com.example.springbootpetproject.service.serviceImplementation;

import com.example.springbootpetproject.entity.City;
import com.example.springbootpetproject.repository.CityRepository;
import com.example.springbootpetproject.service.serviceInterfaces.CityServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<City> findAllCity(Pageable pageable, int pageNumber, String direction, String sort) {
        //Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()).withSort(Sort.by(direction,sort));
        Pageable changePageable = PageRequest.of(pageNumber - 1, pageable.getPageSize()
                ,direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        return cityRepository.findAll(changePageable);
    }

    @Override
    @Transactional
    public void addCity(City city) {
        if(cityIsExist(city.getCityName())){
            throw new IllegalArgumentException("місто вже існує в базі данних");
        }
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
}
