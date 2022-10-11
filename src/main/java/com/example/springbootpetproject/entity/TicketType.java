package com.example.springbootpetproject.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "ticket_type")
//@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

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

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketType)) return false;
        TicketType that = (TicketType) o;
        return id == that.id && Double.compare(that.ticketPriceFactor, ticketPriceFactor) == 0 && Objects.equals(ticketType, that.ticketType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ticketType, ticketPriceFactor);
    }

    @Override
    public String toString() {
        return "TicketType{" +
                "id=" + id +
                ", ticketType='" + ticketType + '\'' +
                ", ticketPriceFactor=" + ticketPriceFactor +
                '}';
    }*/
}
