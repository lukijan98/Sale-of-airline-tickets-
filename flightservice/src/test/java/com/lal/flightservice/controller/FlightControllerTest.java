package com.lal.flightservice.controller;



import com.lal.flightservice.model.Airplane;
import com.lal.flightservice.model.Flight;
import com.lal.flightservice.repository.AirplaneRepository;
import com.lal.flightservice.repository.FlightRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlightControllerTest {


    private static final String FLIGHT_URL = "/flight";

    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AirplaneRepository airplaneRepository;


    @Autowired
    private TestRestTemplate testRestTemplate;

    @Before
    public void setUp() {
        flightRepository.deleteAll();
        airplaneRepository.deleteAll();
    }

    @Test
    public void testSaveFlight(){
        //given
        Airplane airplane = new Airplane();
        airplane.setName("Nikola Tesla");
        airplane.setCapacity(32);
        airplaneRepository.save(airplane);

        Flight flight = new Flight();
        flight.setOrigin("Beograd");
        flight.setDestination("Nis");
        flight.setMiles(100);
        flight.setPrice(50);
        flight.setAirplane(airplane);


        HttpEntity<Flight> request = new HttpEntity<>(flight);
        //when
        ResponseEntity<Flight> response = testRestTemplate
                .exchange(FLIGHT_URL+"/add", HttpMethod.POST, request, Flight.class);

        //then
        //check response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getOrigin()).isEqualTo(flight.getOrigin());
        assertThat(response.getBody().getDestination()).isEqualTo(flight.getDestination());
        assertThat(response.getBody().getMiles()).isEqualTo(flight.getMiles());
        assertThat(response.getBody().getPrice()).isEqualTo(flight.getPrice());
        assertThat(response.getBody().getAirplane().getId()).isEqualTo(airplane.getId());

        //check from database
        Flight flightFromDatabase = flightRepository.findAll().iterator().next();
        assertThat(flightFromDatabase.getOrigin()).isEqualTo(flight.getOrigin());
        assertThat(flightFromDatabase.getDestination()).isEqualTo(flight.getDestination());
        assertThat(flightFromDatabase.getMiles()).isEqualTo(flight.getMiles());
        assertThat(flightFromDatabase.getPrice()).isEqualTo(flight.getPrice());
        assertThat(flightFromDatabase.getAirplane().getId()).isEqualTo(airplane.getId());

    }

    @Test
    public void testUpdateFlight(){
        //given
        Airplane airplane = new Airplane();
        airplane.setName("Nikola Tesla");
        airplane.setCapacity(32);
        airplaneRepository.save(airplane);

        Flight flight = new Flight();
        flight.setOrigin("Beograd");
        flight.setDestination("Nis");
        flight.setMiles(100);
        flight.setPrice(50);
        flight.setAirplane(airplane);
        flightRepository.save(flight);


        Flight updateFlight = new Flight();
        updateFlight.setOrigin("Prag");
        updateFlight.setDestination("Bec");
        updateFlight.setMiles(200);
        updateFlight.setPrice(100);
        updateFlight.setId(flight.getId());

        HttpEntity<Flight> request = new HttpEntity<>(updateFlight);
        //when
        ResponseEntity<Flight> response = testRestTemplate
                .exchange(FLIGHT_URL+"/update", HttpMethod.PUT, request, Flight.class);

        //then
        //check response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getOrigin()).isEqualTo(updateFlight.getOrigin());
        assertThat(response.getBody().getDestination()).isEqualTo(updateFlight.getDestination());
        assertThat(response.getBody().getMiles()).isEqualTo(updateFlight.getMiles());
        assertThat(response.getBody().getPrice()).isEqualTo(updateFlight.getPrice());


        //check from database
        Flight flightFromDatabase = flightRepository.findAll().iterator().next();
        assertThat(flightFromDatabase.getOrigin()).isEqualTo(updateFlight.getOrigin());
        assertThat(flightFromDatabase.getDestination()).isEqualTo(updateFlight.getDestination());
        assertThat(flightFromDatabase.getMiles()).isEqualTo(updateFlight.getMiles());
        assertThat(flightFromDatabase.getPrice()).isEqualTo(updateFlight.getPrice());

    }

    @Test
    public void testFindFlightById() {
        //given
        Airplane airplane = new Airplane();
        airplane.setName("Nikola Tesla");
        airplane.setCapacity(32);
        airplaneRepository.save(airplane);

        Flight flight = new Flight();
        flight.setOrigin("Beograd");
        flight.setDestination("Nis");
        flight.setMiles(100);
        flight.setPrice(50);
        flight.setAirplane(airplane);
        flightRepository.save(flight);
        String url = FLIGHT_URL + "/?id=" + flight.getId();
        //when
        ResponseEntity<Flight> response = testRestTemplate
                .exchange(url, HttpMethod.GET, null, Flight.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertFlight(response.getBody(), flight.getId(),flight.getOrigin(),flight.getDestination(),flight.getMiles(), flight.getPrice(), flight.getAirplane());
    }

    @Test
    public void testFindAllFlightsWithSpecifiedPageable() {
        //given
        Airplane airplane = new Airplane();
        airplane.setName("Nikola Tesla");
        airplane.setCapacity(32);
        airplaneRepository.save(airplane);

        Flight flight1 = new Flight();
        flight1.setOrigin("Beograd");
        flight1.setDestination("Nis");
        flight1.setMiles(100);
        flight1.setPrice(50);
        flight1.setAirplane(airplane);
        flightRepository.save(flight1);

        Flight flight2 = new Flight();
        flight2.setOrigin("Prag");
        flight2.setDestination("Bec");
        flight2.setMiles(200);
        flight2.setPrice(100);
        flight2.setAirplane(airplane);
        flightRepository.save(flight2);


        String url = FLIGHT_URL+"/all" + "?pageNo=1&pageSize=1";
        //when
        ResponseEntity<List<Flight>> response = testRestTemplate
                .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Flight>>() {
                });
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(1);
        assertFlight(response.getBody().get(0), flight2.getId(),flight2.getOrigin(),flight2.getDestination(),flight2.getMiles(), flight2.getPrice(), flight2.getAirplane());
    }

    @Test
    public void testSearch() {
        //given
        Airplane airplane = new Airplane();
        airplane.setName("Nikola Tesla");
        airplane.setCapacity(32);
        airplaneRepository.save(airplane);

        Flight flight1 = new Flight();
        flight1.setOrigin("Beograd");
        flight1.setDestination("Nis");
        flight1.setMiles(100);
        flight1.setPrice(50);
        flight1.setAirplane(airplane);
        flightRepository.save(flight1);

        Flight flight2 = new Flight();
        flight2.setOrigin("Beograd");
        flight2.setDestination("Bec");
        flight2.setMiles(200);
        flight2.setPrice(100);
        flight2.setAirplane(airplane);
        flightRepository.save(flight2);


        String url = FLIGHT_URL+"/search" + "?origin=Beograd&price=100";
        //when
        ResponseEntity<List<Flight>> response = testRestTemplate
                .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Flight>>() {
                });
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(1);
        assertFlight(response.getBody().get(0), flight2.getId(),flight2.getOrigin(),flight2.getDestination(),flight2.getMiles(), flight2.getPrice(), flight2.getAirplane());
    }




    private void assertFlight(Flight flight, Long id, String origin, String destination,int miles,int price,Airplane airplane){
        assertThat(flight.getId()).isEqualTo(id);
        assertThat(flight.getOrigin()).isEqualTo(origin);
        assertThat(flight.getDestination()).isEqualTo(destination);
        assertThat(flight.getMiles()).isEqualTo(miles);
        assertThat(flight.getPrice()).isEqualTo(price);
        assertThat(flight.getAirplane().getId()).isEqualTo(airplane.getId());
    }


}
