package com.example.springbootpetproject.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
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
public class City implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    //@UniqueElements(message = "Emmm")
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
