package com.lal.demo.service.impl;

import com.lal.demo.model.Ticket;
import com.lal.demo.repository.TicketRepository;
import com.lal.demo.service.TicketService;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {

    private TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository){
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public int countTicketByFlightId(Long flightId) {
        return ticketRepository.countTicketByFlightId(flightId);
    }
}
