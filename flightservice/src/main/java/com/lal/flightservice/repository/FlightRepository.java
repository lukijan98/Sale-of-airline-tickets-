package com.lal.flightservice.repository;

import com.lal.flightservice.model.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends PagingAndSortingRepository<Flight, Long> {


    @Query("SELECT f FROM Flight f JOIN f.airplane a WHERE ((:airplaneName is null) or (a.name = :airplaneName)) and ((:origin is null)"
            + " or (f.origin = :origin)) and ((:destination is null) or (f.destination = :destination)) and ((:miles is null) or (f.miles = :miles))"
            + " and ((:price is null) or (f.price = :price)) and ((:flightCanceled is null) or (f.flightCanceled = :flightCanceled))")
    List<Flight> findFlightsByAirplane_NameAndOriginAndDestinationAndMilesAndPriceAndFlightCanceled(@Param("airplaneName")String airplaneName,
                                                                                       @Param("origin") String origin,
                                                                                       @Param("destination") String destination,
                                                                                       @Param("miles") Integer miles,
                                                                                       @Param("price") Integer price,
                                                                                       @Param("flightCanceled")Boolean flightCanceled);
    @Query("SELECT f FROM Flight f WHERE ((:origin is null) or (f.origin = :origin)) and ((:destination is null) or (f.destination = :destination))"
            + " and ((:miles is null) or (f.miles = :miles)) and ((:price is null) or (f.price = :price)) and ((:flightCanceled is null) or (f.flightCanceled = :flightCanceled))")
    List<Flight> findFlightsByOriginAndDestinationAndMilesAndPriceAndFlightCanceled(@Param("origin") String origin,@Param("destination") String destination,
                                                                                    @Param("miles") Integer miles, @Param("price") Integer price,
                                                                                    @Param("flightCanceled")Boolean flightCanceled);
    Page<Flight> findAllByFlightCanceledFalse(Pageable var1);
}
