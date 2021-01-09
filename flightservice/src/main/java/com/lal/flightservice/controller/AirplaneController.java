package com.lal.flightservice.controller;

import com.lal.flightservice.model.Airplane;
import com.lal.flightservice.service.impl.AirplaneServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/airplane")
@EnableWebSecurity
public class AirplaneController {

    private AirplaneServiceImpl airplaneService;

    public AirplaneController(AirplaneServiceImpl airplaneService){
        this.airplaneService = airplaneService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value="/add",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveAirplane(@RequestBody Airplane airplane){ return new ResponseEntity<>(airplaneService.save(airplane), HttpStatus.CREATED);}

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value="/update",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAirplane(@RequestBody Airplane airplane){ return new ResponseEntity<>(airplaneService.update(airplane),HttpStatus.OK); }

    @PreAuthorize("hasRole('ADMIN')")
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
    public ResponseEntity<?> findAllAirplanes(){ return new ResponseEntity<>(airplaneService.findAllAirplanes(),HttpStatus.OK); }
    
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
