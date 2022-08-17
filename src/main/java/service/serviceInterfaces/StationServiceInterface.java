package service.serviceInterfaces;

import entity.Station;

public interface StationServiceInterface {
    boolean addStation(Station station);

    boolean deleteStation(Long id);
}
