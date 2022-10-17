package com.example.springbootpetproject.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Table(name = "train_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Train extends BaseEntity implements Serializable {

    @NotBlank(message = "This field can't be blank")
    @Column(name = "train_number")
    private String trainNumber;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Min(value = 0, message = "Count of compartment seats can't negative")
    @Column(name = "number_of_compartment_seats")
    private int numberOfCompartmentSeats;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Min(value = 0, message = "Count of suite seats can't negative")
    @Column(name = "number_of_suite_seats")
    private int numberOfSuiteSeats;

    //@Value("true")
    @Column(name = "relevant")
    private boolean relevant;

    @OneToMany(mappedBy = "train")
    @JsonManagedReference
    private Set<Route> routeSet;

}
