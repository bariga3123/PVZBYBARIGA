package com.pvz.demo2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordGenerator implements CommandLineRunner {

    @Override
    public void run(String... args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String adminHash = encoder.encode("admin123");
        String userHash  = encoder.encode("user123");

        System.out.println("=====================================");
        System.out.println("ADMIN HASH: " + adminHash);
        System.out.println("USER  HASH: " + userHash);
        System.out.println("=====================================");
    }
}