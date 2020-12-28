package com.lal.flightservice.service;

import com.lal.flightservice.model.Airplane;
import com.lal.flightservice.model.Flight;

import java.util.List;
import java.util.Optional;

public interface FlightService {
    List<Flight> findAllFlights(Integer pageNo, Integer pageSize);
    Optional<Flight> findById(Long id);
    Flight save(Flight flight);
    Flight update(Flight flight);
    void deleteById(Long id);
    List<Flight> search(String airplaneName, String origin, String destination,
                        Integer miles, Integer price);
}
