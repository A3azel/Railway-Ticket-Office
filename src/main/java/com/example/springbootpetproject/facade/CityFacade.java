package com.example.springbootpetproject.facade;

import com.example.springbootpetproject.dto.CityDTO;
import com.example.springbootpetproject.entity.City;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.Format;
import java.text.SimpleDateFormat;

@Component
@Slf4j
public class CityFacade {

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
