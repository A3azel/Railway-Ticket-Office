package entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "train_info")
@Data
@NoArgsConstructor
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "train_number", nullable = false)
    private int trainNumber;

    //@Column(name = "start_station_id", nullable = false)
    @OneToOne
    @JoinColumn(name = "start_station_id")
    private Station startStation;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "travel_time", nullable = false)
    private LocalTime travelTime;

    //@Column(name = "arrival_station_id" ,nullable = false)
    @OneToOne
    @JoinColumn(name = "arrival_station_id")
    private Station arrivalStation;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "number_of_free_seats", nullable = false)
    private int numberOfFreeSeats;

    @Column(name = "prise_of_ticket", nullable = false)
    private BigDecimal priseOfTicket;

    @Column(name = "link_to_rote_page")
    private String linkToRotePage;
}
