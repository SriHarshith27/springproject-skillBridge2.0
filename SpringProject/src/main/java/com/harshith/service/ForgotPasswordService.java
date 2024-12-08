package com.harshith.service;

import com.harshith.model.User;
import com.harshith.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ForgotPasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Send reset link to user's email
    public boolean sendResetLink(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username).stream().findFirst();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            userRepository.save(user);

            String resetLink = "http://localhost:8080/auth/reset-password?token=" + token;
            String subject = "Password Reset Request";
            String body = "Hi " + user.getUsername() + ",\n\n"
                    + "We received a request to reset your password. Click the link below to reset it:\n"
                    + resetLink + "\n\n"
                    + "If you didn't request this, you can ignore this email.\n\n"
                    + "Thanks,\nYour App Team";

            emailService.sendEmail(user.getEmail(), subject, body);
            return true;
        }
        return false;
    }

    // Reset password using token
    public boolean resetPassword(String token, String newPassword) {
        Optional<User> userOptional = userRepository.findByResetToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
