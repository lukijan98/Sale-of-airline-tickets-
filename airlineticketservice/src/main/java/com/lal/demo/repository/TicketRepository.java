package com.lal.demo.repository;

import com.lal.demo.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>{

    int countTicketByFlightId(Long flightId);
    List<Ticket> findAllByFlightId(Long flightId);
}
