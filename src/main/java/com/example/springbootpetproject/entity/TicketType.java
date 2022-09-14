package com.example.springbootpetproject.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "ticket_type")
@Data
@NoArgsConstructor
public class TicketType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ticket_type")
    private String ticketType;

    @Column(name = "ticket_price_factor")
    private double ticketPriceFactor;

    @OneToMany(mappedBy = "ticketType")
    @JsonManagedReference
    private Set<Orders> ordersSet;

    @Override
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
    }
}
