package com.lal.flightservice.controller;

import com.lal.flightservice.model.Airplane;
import com.lal.flightservice.model.Flight;
import com.lal.flightservice.service.impl.FlightServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/flightservice/flight")
public class FlightController {

    private FlightServiceImpl flightService;

    public FlightController(FlightServiceImpl flightService){
        this.flightService = flightService;
    }

    @PostMapping(value="/add",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Flight saveFlight(@RequestBody Flight flight){ return flightService.save(flight); }

    @PutMapping(value="/update",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Flight updateFlight(@RequestBody Flight flight){ return flightService.update(flight); }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteFlight(@PathVariable Long id){
        Optional<Flight> optionalFlight = flightService.findById(id);
        if(optionalFlight.isPresent()) {
            flightService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Flight>> getAllFlights(@RequestParam(defaultValue = "0") Integer pageNo,
                                                      @RequestParam(defaultValue = "10") Integer pageSize)
    {
        List<Flight> list = flightService.findAllFlights(pageNo,pageSize);

        return new ResponseEntity<List<Flight>>(list,new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findFlightById(@RequestParam Long id){
        Optional<Flight> optionalFlight = flightService.findById(id);
        if(optionalFlight.isPresent()) {
            return ResponseEntity.ok(optionalFlight.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Flight> search(@RequestParam(required = false) String airplaneName,
                               @RequestParam(required = false) String origin,
                               @RequestParam(required = false) String destination,
                               @RequestParam(required = false) Integer miles,
                               @RequestParam(required = false) Integer price){
        return flightService.search(airplaneName,origin,destination,miles,price);
    }
}
