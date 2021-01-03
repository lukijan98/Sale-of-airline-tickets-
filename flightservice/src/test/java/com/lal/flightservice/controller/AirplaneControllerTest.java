package com.lal.flightservice.controller;

import com.lal.flightservice.model.Airplane;
import com.lal.flightservice.repository.AirplaneRepository;
import com.lal.flightservice.wrapper.AirplaneListWrapper;
import org.json.JSONObject;
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


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AirplaneControllerTest {

    private static final String AIRPLANE_URL = "/airplane";

    @Autowired
    private AirplaneRepository airplaneRepository;


    @Autowired
    private TestRestTemplate testRestTemplate;

    @Before
    public void setUp() {
        airplaneRepository.deleteAll();
    }


    @Test
    public void testSaveAirplane(){
        //given
        Airplane airplane = new Airplane();
        airplane.setName("Nikola Tesla");
        airplane.setCapacity(32);
        HttpEntity<Airplane> request = new HttpEntity<>(airplane);
        //when
        ResponseEntity<Airplane> response = testRestTemplate
                .exchange(AIRPLANE_URL+"/add", HttpMethod.POST, request, Airplane.class);

        //then
        //check response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getName()).isEqualTo(airplane.getName());
        assertThat(response.getBody().getCapacity()).isEqualTo(airplane.getCapacity());

        //check from database
        Airplane airplaneFromDatabase = airplaneRepository.findAll().iterator().next();
        assertThat(airplaneFromDatabase.getName()).isEqualTo(airplane.getName());
        assertThat(airplaneFromDatabase.getCapacity()).isEqualTo(airplane.getCapacity());

    }

    @Test
    public void testUpdateAirplane(){
        //given
        Airplane airplane = new Airplane();
        airplane.setName("Marko Pantelic");
        airplane.setCapacity(8);
        airplaneRepository.save(airplane);
        String url = AIRPLANE_URL + "/update";
        Airplane updateAirplane = new Airplane();
        updateAirplane.setName("Nikola Zigic");
        updateAirplane.setCapacity(9);
        updateAirplane.setId(airplane.getId());
        HttpEntity<Airplane> request = new HttpEntity<>(updateAirplane);
        //when
        ResponseEntity<Airplane> response = testRestTemplate
                .exchange(url, HttpMethod.PUT, request, Airplane.class);
        //then
        //check response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo(updateAirplane.getName());
        assertThat(response.getBody().getCapacity()).isEqualTo(updateAirplane.getCapacity());

        //check from database
        Airplane airplaneFromDatabase = airplaneRepository.findAll().iterator().next();

        assertThat(airplaneFromDatabase.getName()).isEqualTo(updateAirplane.getName());
        assertThat(airplaneFromDatabase.getCapacity()).isEqualTo(updateAirplane.getCapacity());

    }

    @Test
    public void testDelete() {
        //given
        Airplane airplane = new Airplane();
        airplane.setName("Ljubomir Lazic");
        airplane.setCapacity(12);
        airplaneRepository.save(airplane);
        String url = AIRPLANE_URL + "/" + airplane.getId();
        //when
        ResponseEntity<Void> response = testRestTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(airplaneRepository.count()).isEqualTo(0);
    }

    @Test
    public void testFindAirplaneById() {
        //given
        Airplane airplane = new Airplane();
        airplane.setName("Azura");
        airplane.setCapacity(12);
        airplaneRepository.save(airplane);
        String url = AIRPLANE_URL + "/?id=" + airplane.getId();
        //when
        ResponseEntity<Airplane> response = testRestTemplate
                .exchange(url, HttpMethod.GET, null, Airplane.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertAirplane(response.getBody(), airplane.getId(), airplane.getName(),
                airplane.getCapacity());
    }

    @Test
    public void testFindAllAirplanes() {
        //given
        Airplane airplane = new Airplane();
        airplane.setName("Marija");
        airplane.setCapacity(16);
        airplaneRepository.save(airplane);
        //when
        ResponseEntity<List<Airplane>> response = testRestTemplate
                .exchange(AIRPLANE_URL+"/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<Airplane>>() {
                });
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(1);
        assertAirplane(response.getBody().get(0), airplane.getId(), airplane.getName(),
                airplane.getCapacity());
    }

    private void assertAirplane(Airplane airplane, Long id, String name, int capacity ) {
        assertThat(airplane.getId()).isEqualTo(id);
        assertThat(airplane.getName()).isEqualTo(name);
        assertThat(airplane.getCapacity()).isEqualTo(capacity);
    }

}
