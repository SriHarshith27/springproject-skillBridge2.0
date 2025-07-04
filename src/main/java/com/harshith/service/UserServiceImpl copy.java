package com.harshith.service;

import com.harshith.model.Course;
import com.harshith.model.User;
import com.harshith.repository.UserRepository;
import com.harshith.validation.InputValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Lazy
    private final PasswordEncoder passwordEncoder;
    private final InputValidator inputValidator;
    private final AuditService auditService;

    @Override
    @Transactional
    public User registerUser(User user) {
        // Validate input
        inputValidator.validateUsername(user.getUsername());
        inputValidator.validateEmail(user.getEmail());
        inputValidator.validatePassword(user.getPassword());
        inputValidator.validatePhone(user.getPhone());

        // Sanitize input
        user.setUsername(inputValidator.sanitizeInput(user.getUsername()));
        user.setEmail(inputValidator.sanitizeInput(user.getEmail()));
        user.setPhone(inputValidator.sanitizeInput(user.getPhone()));

        // Check for existing users
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already exists.");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already exists.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        
        // Audit log
        auditService.logUserRegistration(savedUser.getId());
        
        return savedUser;
    }

    @Override
    @Transactional
    public Optional<User> validateUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            // Check if account is locked
            if (!user.isAccountNonLocked()) {
                auditService.logFailedLogin(user.getId(), "Account locked");
                return Optional.empty();
            }
            
            if (passwordEncoder.matches(password, user.getPassword())) {
                // Successful login
                user.resetFailedLoginAttempts();
                user.updateLastLogin();
                userRepository.save(user);
                auditService.logLogin(user.getId(), null); // IP address can be added
                return Optional.of(user);
            } else {
                // Failed login
                user.incrementFailedLoginAttempts();
                userRepository.save(user);
                auditService.logFailedLogin(user.getId(), "Invalid password");
            }
        }
        
        return Optional.empty();
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findByDeletedFalse(pageable);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteUser(Long id, User currentUser) {
        User user = getUserById(id);
        
        // Soft delete
        user.setDeleted(true);
        user.setDeletedAt(LocalDateTime.now());
        user.setDeletedBy(currentUser.getId());
        
        userRepository.save(user);
        
        // Audit log
        auditService.logUserDeletion(currentUser.getId(), id);
    }

    @Override
    @Transactional
    public User updateUser(Long id, User updatedUser, User currentUser) {
        User user = getUserById(id);
        
        // Validate input
        if (updatedUser.getUsername() != null) {
            inputValidator.validateUsername(updatedUser.getUsername());
            user.setUsername(inputValidator.sanitizeInput(updatedUser.getUsername()));
        }
        
        if (updatedUser.getEmail() != null) {
            inputValidator.validateEmail(updatedUser.getEmail());
            user.setEmail(inputValidator.sanitizeInput(updatedUser.getEmail()));
        }
        
        if (updatedUser.getPhone() != null) {
            inputValidator.validatePhone(updatedUser.getPhone());
            user.setPhone(inputValidator.sanitizeInput(updatedUser.getPhone()));
        }
        
        if (updatedUser.getRole() != null) {
            user.setRole(updatedUser.getRole());
        }
        
        User savedUser = userRepository.save(user);
        
        // Audit log
        auditService.logUserUpdate(currentUser.getId(), id);
        
        return savedUser;
    }

    @Override
    @Transactional
    public void updatePassword(User user, String newPassword) {
        inputValidator.validatePassword(newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        // Audit log
        auditService.logPasswordChange(user.getId());
    }

    @Override
    public boolean isPasswordValid(User user, String currentPassword) {
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameAndDeletedFalse(username);
    }

    @Override
    public long countByRole(String role) {
        return userRepository.countByRoleAndDeletedFalse(role);
    }

    @Override
    public List<Course> getEnrolledCoursesByUserId(Long userId) {
        User user = getUserById(userId);
        return new ArrayList<>(user.getEnrolledCourses());
    }
}