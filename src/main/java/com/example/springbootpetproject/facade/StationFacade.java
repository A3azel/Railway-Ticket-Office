package com.example.springbootpetproject.facade;

import com.example.springbootpetproject.dto.StationDTO;
import com.example.springbootpetproject.entity.Station;
import org.springframework.stereotype.Component;

import java.text.Format;
import java.text.SimpleDateFormat;

@Component
public class StationFacade {

    public StationDTO convertStationToStationDTO(Station station){
        StationDTO stationDTO = new StationDTO();
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        stationDTO.setId(station.getId());
        stationDTO.setCreated(formatter.format(station.getCreated()));
        stationDTO.setUpdated(formatter.format(station.getUpdated()));
        stationDTO.setCreatedBy(station.getCreatedBy());
        stationDTO.setLastModifiedBy(station.getLastModifiedBy());
        stationDTO.setStationName(station.getStationName());
        stationDTO.setCityName(station.getCity().getCityName());
        stationDTO.setRelevant(station.isRelevant());
        return stationDTO;
    }
}
