package com.harshith.controller;

import com.harshith.dto.JwtResponse;
import com.harshith.dto.LoginRequest;
import com.harshith.dto.RegisterRequest;
import com.harshith.dto.UserDto;
import com.harshith.model.User;
import com.harshith.service.DtoMapperService;
import com.harshith.service.JwtService;
import com.harshith.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final DtoMapperService dtoMapperService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(registerRequest.getPassword());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPhone(registerRequest.getPhone());
        newUser.setRole(registerRequest.getRole());

        userService.registerUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // Use the generateAccessToken method which only requires UserDetails
        String jwt = jwtService.generateAccessToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    /**
     * Gets the details of the currently authenticated user.
     * @param userDetails The UserDetails object injected by Spring Security.
     * @return A DTO with the current user's information.
     */
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        // The UserDetails object is the currently logged-in user.
        // We find the full User entity from the database using the username.
        User currentUser = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // We convert the User entity to a UserDto to avoid sending sensitive data like the password.
        UserDto userDto = dtoMapperService.toUserDto(currentUser);

        return ResponseEntity.ok(userDto);
    }
}