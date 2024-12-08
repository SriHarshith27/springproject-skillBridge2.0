package com.harshith.config;

import org.springframework.boot.CommandLineRunner; 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.harshith.model.User;
import com.harshith.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class AdminInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner insertAdminUser() {
        return args -> {
            // Check if an admin user already exists
            if (userRepository.findByUsername("admin").isEmpty()) {
                // Create a new admin user
                User admin = new User();
                admin.setUsername("admin");
                
                // Hash the password using BCryptPasswordEncoder
                admin.setPassword(passwordEncoder.encode("admin123"));
                
                admin.setRole("ADMIN");
                admin.setEmail("admin@gmail.com");
                admin.setPhone("9123456787");
                
                // Save the admin user to the database
                userRepository.save(admin);
                
                System.out.println("Admin user created successfully.");
            } else {
                System.out.println("Admin user already exists.");
            }
        };
    }
}
