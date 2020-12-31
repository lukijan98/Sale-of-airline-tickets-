package com.lal.userservice.listener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
@Component
public class Consumer {

    @JmsListener(destination = "userservice.queue")
    public void consume(String id) {

            System.out.println("radi"+ id);


    }

}
