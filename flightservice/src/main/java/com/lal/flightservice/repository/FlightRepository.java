package com.lal.flightservice.repository;

import com.lal.flightservice.model.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends PagingAndSortingRepository<Flight, Long> {
    List<Flight> findByAirplaneNameAndOriginAndDestinationAndMilesAndPriceAndFlightCanceled(String airplaneName, String origin, String destination,
                                                                       Integer miles, Integer price,Boolean flightCanceled);
    Page<Flight> findAllByFlightCanceledFalse(Pageable var1);
}
