package com.example.springbootpetproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "station")
@Data
@NoArgsConstructor
public class Station extends BaseEntity implements Serializable {

    @Column(name = "station_name")
    private String stationName;

    @Column(name = "relevant")
    private boolean relevant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    @JsonBackReference
    private City city;

    @ManyToMany(mappedBy = "stationList")
    private List<Route> routeList;
}
