package com.lal.flightservice.controller;

import com.lal.flightservice.model.Airplane;
import com.lal.flightservice.model.Flight;
import com.lal.flightservice.service.impl.FlightServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/flight")
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
            Flight flight = optionalFlight.get();
            if(flight.getAvailableSeats()!=flight.getAirplane().getCapacity()){
                /*
                activeMq za povracaj novca
                posalti mejl useru da je otkazan let i oduzeti mu milje

                 */
                flight.setFlightCanceled(true);
                flightService.update(flight);
            }
            //flightService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Flight>> getAllFlights(@RequestParam(defaultValue = "0") Integer pageNo,
                                                      @RequestParam(defaultValue = "10") Integer pageSize)
    {
        //List<Flight> list = flightService.findAllFlights(pageNo,pageSize);
        List<Flight> list = flightService.findAllByFlightCanceledFalse(pageNo,pageSize);
//        for(Flight flight:list){
//            if(flight.isFlightCanceled()){
//                list.remove(flight);
//            }
//        }
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
                               @RequestParam(required = false) Integer price,
                               @RequestParam(required = false) Boolean flightCanceled){
        return flightService.search(airplaneName,origin,destination,miles,price,flightCanceled);
    }

    @PostMapping(value = "/checkCapacity",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkCapacity(@RequestHeader Long flightId){
        boolean check = false;
        Optional<Flight> optionalFlight = flightService.findById(flightId);
        int price = 0;
        int miles = 0;
        if(optionalFlight.isPresent()) {
            Flight flight = optionalFlight.get();
            price = flight.getPrice();
            miles = flight.getMiles();
            if (flight.getAvailableSeats()>0) {
                check = true;
            } else check = false;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("checkCapacity",check);
        map.put("price",price);
        map.put("miles",miles);
        return new ResponseEntity<Map<String,Object>>(map,new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping(value = "/updateCapacity",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCapacity(@RequestHeader Long flightId){
        Optional<Flight> optionalFlight = flightService.findById(flightId);
        if(optionalFlight.isPresent()) {
            Flight flight = optionalFlight.get();
            int mesta = flight.getAvailableSeats()-1;
            System.out.println("updateovana vrednost "+ mesta);
            flight.setAvailableSeats(mesta);
            System.out.println("setovana vrednost "+ flight.getAvailableSeats());
            flightService.update(flight);
            return ResponseEntity.ok(flight);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
