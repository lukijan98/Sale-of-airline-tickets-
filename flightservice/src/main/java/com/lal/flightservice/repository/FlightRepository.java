package com.lal.flightservice.repository;

import com.lal.flightservice.model.Flight;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends PagingAndSortingRepository<Flight, Long> {
    List<Flight> findByAirplaneNameAndOriginAndDestinationAndMilesAndPrice(String airplaneName, String origin, String destination,
                                                                       Integer miles, Integer price);
}
