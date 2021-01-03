package com.lal.demo.service;

import com.lal.demo.model.Ticket;

import java.util.List;

public interface TicketService {

    Ticket save(Ticket ticket);

    int countTicketByFlightId(Long flightId);

    List<Ticket> findAllByFlightId(Long flightid);
}
