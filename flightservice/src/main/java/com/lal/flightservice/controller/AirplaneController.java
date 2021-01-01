package com.lal.flightservice.controller;

import com.lal.flightservice.model.Airplane;
import com.lal.flightservice.service.impl.AirplaneServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/airplane")
public class AirplaneController {

    private AirplaneServiceImpl airplaneService;

    public AirplaneController(AirplaneServiceImpl airplaneService){
        this.airplaneService = airplaneService;
    }

    @PostMapping(value="/add",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Airplane saveFlight(@RequestBody Airplane airplane){ return airplaneService.save(airplane); }

    @PutMapping(value="/update",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Airplane updateFlight(@RequestBody Airplane airplane){ return airplaneService.update(airplane); }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteAirplane(@PathVariable Long id){
        Optional<Airplane> optionalAirplane = airplaneService.findById(id);
        if(optionalAirplane.isPresent()) {
            airplaneService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping(value = "/all",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Airplane> findAllAirplanes(){ return airplaneService.findAllAirplanes(); }
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAirplaneById(@RequestParam Long id){
        Optional<Airplane> optionalAirplane = airplaneService.findById(id);
        if(optionalAirplane.isPresent()) {
            return ResponseEntity.ok(optionalAirplane.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
