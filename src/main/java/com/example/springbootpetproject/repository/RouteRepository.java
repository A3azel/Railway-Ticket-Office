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
import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route,Long> {
    Page<Route> findAllByStartStation_City_CityNameAndArrivalStation_City_CityNameAndDepartureTimeBetween(
            String senderCity, String cityOfArrival, LocalDateTime selectedDates, LocalDateTime finalDates, Pageable pageable);

    Page<Route> findAllByStartStation_RelevantIsTrueAndArrivalStation_RelevantIsTrueAndStartStation_City_CityNameAndArrivalStation_City_CityNameAndDepartureTimeBetween(
            String senderCity, String cityOfArrival, LocalDateTime selectedDates, LocalDateTime finalDates, Pageable pageable);

    /*Page<Route> findAllByStartStation_City_RelevantAndArrivalStation_City_RelevantAndStartStation_City_CityNameAndArrivalStation_City_CityNameAndDepartureTimeBetween(
            boolean b1, boolean b2, String senderCity, String cityOfArrival, LocalDateTime selectedDates, LocalDateTime finalDates, Pageable pageable);

    List<Route> findAllByStartStation_City_RelevantAndArrivalStation_City_RelevantAndStartStation_City_CityNameAndArrivalStation_City_CityName(boolean b1, boolean b2, String senderCity, String cityOfArrival);*/

    Page<Route> findAll(Pageable pageable);
    Optional<Route> findById(Long Id);
    Route findRouteById(Long Id);

    /**
     * Проверка наличия мест встроена в UPDATE: иначе два параллельных заказа оба видят
     * свободные места и перепродают их, а запрос без условия уводит счётчик в минус.
     * Возвращает число затронутых строк: 0 — мест не хватило.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE route SET number_of_compartment_free_seats = (number_of_compartment_free_seats - :countOfPurchasedTickets) " +
            "where id = :routeId AND number_of_compartment_free_seats >= :countOfPurchasedTickets",
            nativeQuery = true)
    int reduceNumberOfCompartmentFreeSeats(@Param("routeId") Long routeId,
                                            @Param("countOfPurchasedTickets") int countOfPurchasedTickets);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE route SET number_of_suite_free_seats = (number_of_suite_free_seats - :countOfPurchasedTickets) " +
            "where id = :routeId AND number_of_suite_free_seats >= :countOfPurchasedTickets",
            nativeQuery = true)
    int reduceNumberOfSuiteFreeSeats(@Param("routeId") Long routeId,
                                            @Param("countOfPurchasedTickets") int countOfPurchasedTickets);




}
