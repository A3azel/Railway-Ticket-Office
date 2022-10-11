package com.example.springbootpetproject.repository;

import com.example.springbootpetproject.entity.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface StationRepository extends JpaRepository<Station,Long> {
    Page<Station> getStationByCity_CityName(String cityName,Pageable pageable);

    Station findByStationName(String stationName);

    void deleteById(Long id);

    Station findStationById(Long id);

    @Modifying
    @Query(value = "UPDATE station_list SET relevant = :changedRelevant WHERE id = :id", nativeQuery = true)
    void setStationRelevant(@Param("changedRelevant") boolean changedRelevant, @Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE station_list SET relevant = :changedRelevant WHERE id = :id", nativeQuery = true)
    void setStationRelevantByCity(@Param("changedRelevant") boolean changedRelevant, @Param("id") Long id);
}
