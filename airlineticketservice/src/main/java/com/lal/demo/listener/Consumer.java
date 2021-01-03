package com.lal.demo.listener;

import com.lal.demo.model.Ticket;
import com.lal.demo.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class Consumer {

    @Autowired
    TicketRepository ticketRepository;

    @JmsListener(destination = "airlineticketservice.queue")
    public void consume(String flightId) {
        Long flightIdlong = Long.parseLong(flightId);
        List<Ticket> ticketsToCanel = ticketRepository.findAllByFlightId(flightIdlong);
        for(Ticket t: ticketsToCanel){
            t.setTicketCanceled(true);
            ticketRepository.save(t);
        }
            System.out.println("Tickets canceled");


    }


}
