package com.example.springbootpetproject.repository;

import com.example.springbootpetproject.entity.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RouteRepository extends JpaRepository<Route,Long> {
    Page<Route> findAllByStartStation_City_CityNameAndArrivalStation_City_CityNameAndDepartureTimeBetween(
            String senderCity, String cityOfArrival, LocalDateTime selectedDates, LocalDateTime finalDates, Pageable pageable);

    Page<Route> findAllByStartStation_City_RelevantAndArrivalStation_City_RelevantAndStartStation_City_CityNameAndArrivalStation_City_CityNameAndDepartureTimeBetween(
            boolean b1, boolean b2, String senderCity, String cityOfArrival, LocalDateTime selectedDates, LocalDateTime finalDates, Pageable pageable);
    Page<Route> findAll(Pageable pageable);
    Route findRouteById(Long Id);

    @Modifying
    @Query(value = "UPDATE route SET number_of_compartment_free_seats = (number_of_compartment_free_seats - :countOfPurchasedTickets) where id = :routeId",
            nativeQuery = true)
    void reduceNumberOfCompartmentFreeSeats(@Param("routeId") Long routeId,
                                            @Param("countOfPurchasedTickets") int countOfPurchasedTickets);

    @Modifying
    @Query(value = "UPDATE route SET number_of_suite_free_seats = (number_of_suite_free_seats - :countOfPurchasedTickets) where id = :routeId",
            nativeQuery = true)
    void reduceNumberOfSuiteFreeSeats(@Param("routeId") Long routeId,
                                            @Param("countOfPurchasedTickets") int countOfPurchasedTickets);




}
