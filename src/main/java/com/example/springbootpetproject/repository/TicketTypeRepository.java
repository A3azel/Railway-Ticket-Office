package com.example.springbootpetproject.repository;

import com.example.springbootpetproject.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType,Long> {

    boolean deleteTicketTypeByTicketType(String ticketName);

    @Modifying
    @Query(value = "UPDATE ticket_type SET ticket_price_factor = :priceFactor WHERE ticket_type = :ticketTypeName"
            , nativeQuery = true)
    boolean setTicketPriceFactor(@Param("ticketTypeName") String ticketTypeName,
                                 @Param("priceFactor") float priceFactor);

}
