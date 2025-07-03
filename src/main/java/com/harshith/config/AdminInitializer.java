package com.harshith.config;

import com.harshith.model.User;
import com.harshith.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner insertAdminUser() {
        return args -> {
            // Check if an admin user already exists by email, which should be unique
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");
                admin.setEmail("admin@skillbridge.com");
                admin.setPhone("9123456787");

                userRepository.save(admin);
                System.out.println("Default admin user created successfully.");
            } else {
                System.out.println("Admin user already exists.");
            }
        };
    }
}
