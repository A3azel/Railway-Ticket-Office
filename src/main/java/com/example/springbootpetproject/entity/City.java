package com.example.springbootpetproject.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "cities")
//@Data
//@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@ToString
public class City extends BaseEntity implements Serializable {

    @NotBlank(message = "This field can't be blank")
    @Column(name = "city_name")
    private String cityName;

    @Column(name = "relevant")
    private boolean relevant;

    @OneToMany(mappedBy = "city",fetch = FetchType.LAZY)
    @JsonManagedReference
    //No
    //@JsonIgnore
    private Set<Station> stationSet;
}
