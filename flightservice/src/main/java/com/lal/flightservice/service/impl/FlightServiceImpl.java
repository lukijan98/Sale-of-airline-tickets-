package com.lal.flightservice.service.impl;

import com.lal.flightservice.model.Airplane;
import com.lal.flightservice.model.Flight;
import com.lal.flightservice.repository.FlightRepository;
import com.lal.flightservice.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Queue;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FlightServiceImpl implements FlightService {
    
    private FlightRepository flightRepository;
    @Autowired
    JmsTemplate jmsTemplate;
    @Autowired
    Queue userserviceQueue;
    
    public FlightServiceImpl(FlightRepository flightRepository){
        this.flightRepository = flightRepository;
    }
    
    @Override
    public List<Flight> findAllFlights(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo,pageSize);

        Page<Flight> pagedResult = flightRepository.findAll(paging);
        
        if(pagedResult.hasContent()){
            return pagedResult.getContent();
        }else{
            return new ArrayList<Flight>();
        }
    }

    @Override
    public Optional<Flight> findById(Long id) {
        return flightRepository.findById(id);
    }

    @Override
    public Flight save(Flight flight) {
        return flightRepository.save(flight);
    }

    @Override
    public Flight update(Flight flight) {
        Long id = flight.getId();
        Optional<Flight> optionalFlight = flightRepository.findById(id);
        Flight oldFlight = optionalFlight.get();
        if(flight.getAirplane()!=null){
            oldFlight.setAirplane(flight.getAirplane());
        }
        if(flight.getOrigin()!=null){
            oldFlight.setOrigin(flight.getOrigin());
        }
        if(flight.getDestination()!=null){
            oldFlight.setOrigin(flight.getOrigin());
        }
        if(flight.getMiles()!=0){
            oldFlight.setOrigin(flight.getOrigin());
        }
        if(flight.getPrice()!=0){
            oldFlight.setOrigin(flight.getOrigin());
        }
        return flightRepository.save(oldFlight);
    }

    @Override
    public void deleteById(Long id) {

        try{
            jmsTemplate.convertAndSend(userserviceQueue, Long.toString(id));

            flightRepository.deleteById(id);
        } catch (JmsException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Flight> search(String airplaneName, String origin, String destination, Integer miles, Integer price) {
        return flightRepository.findByAirplaneNameAndOriginAndDestinationAndMilesAndPrice(airplaneName,origin,destination,
                                                                                        miles,price);
    }
}
