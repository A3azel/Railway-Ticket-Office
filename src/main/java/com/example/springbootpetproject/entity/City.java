package com.example.springbootpetproject.entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "cities")
@Data
@NoArgsConstructor
public class City implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "city_name")
    private String cityName;

    //@OneToMany(mappedBy = "cities",cascade = CascadeType.ALL)
    @OneToMany(mappedBy = "city")
    private Set<Station> stationSet;
}
