package com.lal.userservice.service.impl;

import com.lal.userservice.model.ConfirmationToken;
import com.lal.userservice.model.User;
import com.lal.userservice.repository.ConfirmationTokenRepository;
import com.lal.userservice.repository.UserRepository;
import com.lal.userservice.service.EmailSenderService;
import com.lal.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    public UserServiceImpl(UserRepository userRepository, ConfirmationTokenRepository confirmationTokenRepository)
    {
        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @Override
    public User save(User user) {

        User existingUser = userRepository.findByEmailIgnoreCase(user.getEmail());

        if(existingUser != null)
        {
            System.out.println("User with that email already exists");
            return null;
        }
        else
        {
            userRepository.save(user);

            ConfirmationToken confirmationToken = new ConfirmationToken(user);

            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("apetrovic8517rn@raf.rs");
            mailMessage.setText("To confirm your account, please click here : "
                    +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);


        }
        System.out.println("User created, left to confirm");
        return null;
    }

    @Override
    public User confirm(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            user.setEnabled(true);
            userRepository.save(user);
            System.out.println("User is confirmed");
        }
        else
        {
            System.out.println("Link is invalid or broken");
        }


        return null;
    }
}
