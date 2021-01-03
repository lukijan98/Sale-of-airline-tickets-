package com.lal.flightservice.service.impl;

import com.lal.flightservice.model.Airplane;
import com.lal.flightservice.repository.AirplaneRepository;
import com.lal.flightservice.service.AirplaneService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirplaneServiceImpl implements AirplaneService {
    
    private AirplaneRepository airplaneRepository;
    
    public AirplaneServiceImpl(AirplaneRepository airplaneRepository){
        this.airplaneRepository = airplaneRepository;
    }

    @Override
    public Optional<Airplane> findById(Long id) {
        return airplaneRepository.findById(id);
    }

    @Override
    public Airplane update(Airplane airplane) {
        Long id = airplane.getId();
        Optional<Airplane> optionalAirplane = airplaneRepository.findById(id);
        Airplane oldAirplane = optionalAirplane.get();
        if(airplane.getName()!=null){
            oldAirplane.setName(airplane.getName());
        }
        if(airplane.getCapacity()!=0){
            oldAirplane.setCapacity(airplane.getCapacity());
        }
        return airplaneRepository.save(oldAirplane);
    }

    @Override
    public List<Airplane> findAllAirplanes() {
        return (List<Airplane>) airplaneRepository.findAll();
    }

    @Override
    public Airplane save(Airplane airplane) {
        return airplaneRepository.save(airplane);
    }

    @Override
    public void deleteById(Long id) {
        airplaneRepository.deleteById(id);
    }
}
