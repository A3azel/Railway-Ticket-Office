package com.example.springbootpetproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "station_list")
@Data
@NoArgsConstructor
/*@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor*/
@ToString
public class Station implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "station_name")
    private String stationName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    //backup option (not recommended)
    //@JsonIgnore
    @JsonBackReference
    private City city;
}
