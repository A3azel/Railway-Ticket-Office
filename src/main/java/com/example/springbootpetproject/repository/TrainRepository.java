package com.example.springbootpetproject.repository;

import com.example.springbootpetproject.entity.City;
import com.example.springbootpetproject.entity.Station;
import com.example.springbootpetproject.entity.Train;
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
    List<Train> getByStartStation_City_CityNameAndArrivalStation_City_CityNameAndDepartureTime(String senderCity, String cityOfArrival, LocalDateTime departureTime);
    List<Train> getAllByStartStation_StationNameAndArrivalStation_City_CityName(String startStation,String cityOfArrival);
    List<Train> getAllByStartStation_StationNameAndArrivalStation_StationName(String startStation, String ArrivalStation);



    @Modifying
    @Query(
            value = "UPDATE train_info SET number_of_free_seats = (number_of_free_seats - :countOfPurchasedTickets) where train_number = :trainNumber",
            nativeQuery = true)
    void reduceTheNumberOfSeats(
            @Param("trainNumber") String trainNumber,
            @Param("countOfPurchasedTickets") int countOfPurchasedTickets);
}
