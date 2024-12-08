package com.harshith.controller;

import com.harshith.model.User;
import com.harshith.service.EmailService;
import com.harshith.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class AuthController {
 
    @Autowired
    private UserService userService;
    
    
    
    @Autowired
    private EmailService emailService;

    // GET endpoint to display the registration page
    @GetMapping("/register")
    public String showRegisterPage() {
        return "common/register"; // Maps to /WEB-INF/views/common/register.jsp
    }

    // POST endpoint for processing registration
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (userService.isUsernameUnique(user.getUsername())) {
            userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        }
    }
 

    // GET endpoint to display the login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "common/login"; // Maps to /WEB-INF/views/common/login.jsp
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session) {

        Optional<User> user = userService.validateUser(username, password);
        if (user.isPresent()) {
            User loggedInUser = user.get();
            session.setAttribute("username", username);
            session.setAttribute("role", loggedInUser.getRole());
            session.setAttribute("userId", loggedInUser.getId()); // Add userId to session

            String role = loggedInUser.getRole();
            if ("ADMIN".equalsIgnoreCase(role)) {
                return ResponseEntity.ok("/admin/dashboard");
            } else if ("MENTOR".equalsIgnoreCase(role)) {
                return ResponseEntity.ok("/mentor/dashboard");
            } else {
                return ResponseEntity.ok("/user/dashboard");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // Invalidate the session
        return ResponseEntity.ok("Logged out successfully");
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String username) {
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);
            userService.updateUser(user);

            String resetLink = "http://localhost:8080/auth/reset-password?token=" + resetToken;
            String subject = "Password Reset Request";
            String message = "Hi " + user.getUsername() + ",\n\n"
                    + "You have requested to reset your password. Click the link below to reset it:\n"
                    + resetLink + "\n\n"
                    + "If you did not request this, please ignore this email.";

            emailService.sendEmail(user.getEmail(), subject, message);

            return ResponseEntity.ok("Password reset email sent successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with the given username.");
        }
    }

    
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "common/forgot-password"; // Map to JSP page
    }

    
    // Endpoint to reset the password
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {

        Optional<User> userOptional = userService.findByResetToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userService.updatePassword(user, newPassword);
            user.setResetToken(null);
            userService.updateUser(user);

            return ResponseEntity.ok("Password reset successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
        }
    }

    
}
