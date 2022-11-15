package com.example.springbootpetproject.repository;

import com.example.springbootpetproject.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City,Long> {

    Page<City> findAll(Pageable pageable);

    City findCityByCityName(String cityName);

    Optional<City> findByCityName(String cityName);

    boolean existsCityByCityName(String cityName);

    City findCityById(Long id);

    @Modifying
    @Query(value = "UPDATE city SET relevant = :changedRelevant WHERE id = :id", nativeQuery = true)
    void setCityRelevant(@Param("changedRelevant") boolean changedRelevant, @Param("id") Long id);
}
