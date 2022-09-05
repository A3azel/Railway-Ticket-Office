package com.example.springbootpetproject.repository;

import com.example.springbootpetproject.entity.City;
import com.example.springbootpetproject.entity.Station;
import com.example.springbootpetproject.entity.Train;
import com.example.springbootpetproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrainRepository extends JpaRepository<Train,Long> {
    Train getTrainByTrainNumber(String trainNumber);
    List<Train> getAllByStartStation_City_CityNameAndArrivalStation_City_CityName(String senderCity, String cityOfArrival);

    Page<Train> findAllByStartStation_City_CityNameAndArrivalStation_City_CityNameAndDepartureTimeBetween(
            String senderCity, String cityOfArrival, LocalDateTime selectedDates, LocalDateTime finalDates, Pageable pageable);

    List<Train> getAllByStartStation_StationNameAndArrivalStation_City_CityName(String startStation,String cityOfArrival);
    List<Train> getAllByStartStation_StationNameAndArrivalStation_StationName(String startStation, String ArrivalStation);

    Page<Train> getAllByStartStation_City_CityNameAndArrivalStation_City_CityName(String senderCity, String cityOfArrival, Pageable pageable);

    Page<Train> findAll(Pageable pageable);

    Train findTrainById(Long id);

    Train findTrainByTrainNumber(String trainNumber);

    void deleteById(Long id);

    @Modifying
    @Query(
            value = "UPDATE train_info SET number_of_free_seats = (number_of_free_seats - :countOfPurchasedTickets) where train_number = :trainNumber",
            nativeQuery = true)
    void reduceTheNumberOfSeats(
            @Param("trainNumber") String trainNumber,
            @Param("countOfPurchasedTickets") int countOfPurchasedTickets);
}
