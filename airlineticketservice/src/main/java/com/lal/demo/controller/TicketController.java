package com.lal.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lal.demo.model.Ticket;
import com.lal.demo.service.impl.TicketServiceImpl;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class TicketController {

    private TicketServiceImpl ticketService;

    public TicketController(TicketServiceImpl ticketService){
        this.ticketService = ticketService;
    }

    @PostMapping(value="/buyTicket",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> buyTicket(@RequestBody Ticket ticket,@RequestHeader(value = "Authorization") String token){
        ObjectMapper objectMapper = new ObjectMapper();
        //int nbSoldTickets = ticketService.countTicketByFlightId(ticket.getFlightId());
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String flightserviceUri=null;
        String userserviceUri=null;
        HttpPost zuul = new HttpPost("http://localhost:8762/actuator/routes");
        try {
            response = client.execute(zuul);
            String result = EntityUtils.toString(response.getEntity());
            Map<String, String> map = objectMapper.readValue(result, Map.class);
            String flightservice = "flightservice";
            String userservice = "userservice";
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getValue().equals(flightservice)) {
                    flightserviceUri=entry.getKey();
                }
                if (entry.getValue().equals(userservice)) {
                    userserviceUri=entry.getKey();
                }
            }
            flightserviceUri = flightserviceUri.replace("**","");
            userserviceUri = userserviceUri.replace("**","");
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpPost httpPost = new HttpPost("http://localhost:8762"+flightserviceUri+"flight/checkCapacity");
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        //httpPost.setHeader("nbSoldTickets",Integer.toString(nbSoldTickets));
        httpPost.setHeader("flightId",Long.toString(ticket.getFlightId()));
        boolean checkCapacity=false;
        double price=0;
        int miles=0;
        try {
            response = client.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity());
            Map<String, Object> map = objectMapper.readValue(result, Map.class);
            checkCapacity = (boolean) map.get("checkCapacity");
            price = (int) map.get("price");
            miles = (int) map.get("miles");
            System.out.println("ima mesta i cena je " + checkCapacity + price);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(checkCapacity){
            HttpPost httpPost1 = new HttpPost("http://localhost:8762"+userserviceUri+"userInfo");
            httpPost1.setHeader("Accept", "application/json");
            httpPost1.setHeader("Content-type", "application/json");
            httpPost1.setHeader("Authorization","Bearer "+token);
            httpPost1.setHeader("userId",Long.toString(ticket.getUserId()));
            String rank = null;
            boolean hasCreditCards = false;
            try {
                response = client.execute(httpPost1);
                String result = EntityUtils.toString(response.getEntity());
                Map<String, Object> map = objectMapper.readValue(result, Map.class);
                hasCreditCards = (boolean) map.get("hasCreditCards");
                rank = (String) map.get("rank");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(hasCreditCards){
                switch (rank){
                    case "GOLD":
                        price = price * 0.8;
                        break;
                    case "SILVER":
                        price = price * 0.9;
                        break;
                }
            }
            System.out.println("NOVA CENA JE " + price);
            HttpPost httpPost2 = new HttpPost("http://localhost:8762"+flightserviceUri+"flight/updateCapacity");
            httpPost2.setHeader("Accept", "application/json");
            httpPost2.setHeader("Content-type", "application/json");
            httpPost2.setHeader("flightId",Long.toString(ticket.getFlightId()));
            try {
                response = client.execute(httpPost2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            HttpPost httpPost3 = new HttpPost("http://localhost:8762"+userserviceUri+"updatemilesandrank");
            httpPost3.setHeader("Accept", "application/json");
            httpPost3.setHeader("Content-type", "application/json");
            httpPost3.setHeader("Authorization","Bearer "+token);
            httpPost3.setHeader("miles",Integer.toString(miles));
            try {
                response = client.execute(httpPost3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok().build();
    }

//    @GetMapping(value = "/getTickets",
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<Ticket>> getAllTickets()
//    {
//        List<Ticket> list = flightService.findAllFlights(pageNo,pageSize);
//
//        return new ResponseEntity<List<Flight>>(list,new HttpHeaders(), HttpStatus.OK);
//    }
}
