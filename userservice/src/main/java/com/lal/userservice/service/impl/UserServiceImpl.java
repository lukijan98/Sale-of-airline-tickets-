package com.lal.userservice.service.impl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.lal.userservice.model.ConfirmationToken;
import com.lal.userservice.model.User;
import com.lal.userservice.repository.ConfirmationTokenRepository;
import com.lal.userservice.repository.UserRepository;
import com.lal.userservice.service.EmailSenderService;
import com.lal.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import static com.lal.userservice.security.SecurityConstants.*;

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
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
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
            if(token.getEmailToSet()==null)
                user.setEnabled(true);
            else
                user.setEmail(token.getEmailToSet());
            userRepository.save(user);
            System.out.println("Email link confirmed");
        }
        else
        {
            System.out.println("Link is invalid or broken");
        }


        return null;
    }

    @Override
    public User update(User user,String token) {

        String email = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build()
                    .verify(token.replace(TOKEN_PREFIX, "")).getSubject();
        User oldUser = userRepository.findByEmailIgnoreCase(email);
        if(user.getFirstname()!=null)
            oldUser.setFirstname(user.getFirstname());
        if(user.getLastname()!=null)
            oldUser.setLastname(user.getLastname());
        if(user.getPassportNumber()!=null)
            oldUser.setPassportNumber(user.getPassportNumber());
        if(user.getPassword()!=null)
        {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(user.getPassword());
            oldUser.setPassword(encodedPassword);
        }
        if(user.getEmail()!=null)
        {
           // oldUser.setEnabled(false);
           // oldUser.setEmail(user.getEmail());
            ConfirmationToken confirmationToken = new ConfirmationToken(oldUser);
            confirmationToken.setEmailToSet(user.getEmail());
            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("apetrovic8517rn@raf.rs");
            mailMessage.setText("To confirm your account, please click here : "
                    +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);
        }

        userRepository.save(oldUser);




        return null;
    }
}
