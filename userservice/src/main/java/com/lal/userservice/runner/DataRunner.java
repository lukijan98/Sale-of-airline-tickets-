package com.lal.userservice.runner;

import com.lal.userservice.model.Admin;
import com.lal.userservice.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Profile({"default"})
@Component
public class DataRunner implements CommandLineRunner {

    private AdminRepository adminRepository;

    public DataRunner(AdminRepository adminRepository){
        this.adminRepository=adminRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Admin admin = new Admin();
        admin.setUsername("gospodar");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("sifra");
        admin.setPassword(encodedPassword);
        adminRepository.save(admin);


    }
}
