package com.example.springbootpetproject.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "cities")
@Data
@NoArgsConstructor
/*@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString*/
public class City implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "city_name")
    private String cityName;

    @OneToMany(mappedBy = "city",fetch = FetchType.LAZY)
    @JsonManagedReference
    //No
    //@JsonIgnore
    private Set<Station> stationSet;
}
