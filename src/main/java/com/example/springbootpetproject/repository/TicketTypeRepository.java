package com.example.springbootpetproject.repository;

import com.example.springbootpetproject.entity.TicketType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType,Long> {

    void deleteTicketTypeByTicketTypeName(String ticketName);

    @Modifying
    @Query(value = "UPDATE ticket_type SET ticket_price_factor = :priceFactor WHERE ticket_type = :ticketTypeName"
            , nativeQuery = true)
    void setTicketPriceFactor(@Param("ticketTypeName") String ticketTypeName,
                                 @Param("priceFactor") float priceFactor);

    Page<TicketType> findAll(Pageable pageable);

    Page<TicketType> findAllByRelevantTrue(Pageable pageable);

    TicketType findTicketTypeByTicketTypeName(String ticketType);

    TicketType findTicketTypeById(Long id);

    @Modifying
    @Query(value = "UPDATE ticket_type SET relevant = :changedRelevant WHERE id = :id", nativeQuery = true)
    void setTicketRelevant(@Param("changedRelevant") boolean changedRelevant, @Param("id") Long id);

}
