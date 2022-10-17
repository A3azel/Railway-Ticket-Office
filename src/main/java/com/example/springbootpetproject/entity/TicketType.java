package com.example.springbootpetproject.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "ticket_type")
//@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TicketType extends BaseEntity implements Serializable {

    @NotBlank(message = "This field can't be blank")
    @Column(name = "ticket_type")
    private String ticketTypeName;

    @NotNull(message = "This field can't be blank")
    @DecimalMin(value = "0.00", inclusive = false, message = "Price factor can't negative")
    @Digits(integer = 1, fraction = 2, message = "Example 1.23")
    @Column(name = "ticket_price_factor")
    private double ticketPriceFactor;

    @Column(name = "relevant")
    private boolean relevant;

    @OneToMany(mappedBy = "ticketType")
    @JsonManagedReference
    private Set<Orders> ordersSet;
}
