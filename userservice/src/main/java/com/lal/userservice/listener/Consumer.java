package com.lal.userservice.listener;
import com.lal.userservice.model.User;
import com.lal.userservice.repository.UserRepository;
import com.lal.userservice.service.EmailSenderService;
import com.lal.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class Consumer {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private UserRepository userRepository;

    @JmsListener(destination = "userservice.queue")
    public void consume(Map<String,Integer> map) {

          System.out.println(map);
        Map.Entry<String, Integer> entry = map.entrySet().stream().findFirst().get();
        Long key = Long.parseLong(entry.getKey());
        int value = entry.getValue();
        Optional<User> optionalUser = userRepository.findById(key);
        User user = optionalUser.get();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Da ne verujes!");
        mailMessage.setFrom("apetrovic8517rn@raf.rs");
        mailMessage.setText("Let otkazan zbog korone");
        emailSenderService.sendEmail(mailMessage);

        int newMiles = user.getMiles()-value;

        if((newMiles>1000)&&(newMiles<=10000))
            user.setRank("SILVER");
        else if(newMiles>10000)
            user.setRank("GOLD");
        else
            user.setRank("BRONZE");
        //FALI ZA RANK !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        user.setMiles(newMiles);
        userRepository.save(user);

    }

}
