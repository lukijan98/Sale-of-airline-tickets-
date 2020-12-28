package com.lal.flightservice.service;

import com.lal.flightservice.model.Airplane;

import java.util.List;
import java.util.Optional;

public interface AirplaneService {

    Optional<Airplane> findById(Long id);
    Airplane update(Airplane airplane);
    List<Airplane> findAllAirplanes();
    Airplane save(Airplane airplane);
    void deleteById(Long id);
}
