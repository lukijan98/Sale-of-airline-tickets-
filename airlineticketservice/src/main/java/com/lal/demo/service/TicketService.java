package com.lal.demo.service;

import com.lal.demo.model.Ticket;

public interface TicketService {

    Ticket save(Ticket ticket);

    int countTicketByFlightId(Long flightId);
}
