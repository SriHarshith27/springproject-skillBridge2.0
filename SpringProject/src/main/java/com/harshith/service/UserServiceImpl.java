package com.harshith.service;

import com.harshith.model.Assignment;
import com.harshith.model.Course;
import com.harshith.model.User;
import com.harshith.repository.AssignmentRepository;
import com.harshith.repository.UserRepository;
import java.util.Collections;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.sql.Blob;
import java.sql.SQLException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; 

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    
    @Override
    public long countByRole(String role) {
        return userRepository.countByRole(role);
    }

    
    
    @Override
    public User updateUser(Long id, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setPhone(updatedUser.getPhone());
            user.setRole(updatedUser.getRole());
            user.setPassword(updatedUser.getPassword()); // Plain password (for non-encrypted passwords)
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username).stream().findFirst();
    }

    @Override
    public Optional<User> validateUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username).stream().findFirst();
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    @Override
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public boolean isUsernameUnique(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean isPasswordValid(User user, String currentPassword) {
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public List<Assignment> getAssignmentsByUserId(Long userId) {
        return assignmentRepository.findBySubmittedByUserId(userId);
    }

    
    @Override
    public void uploadStudentAssignment(Long assignmentId, Long userId, MultipartFile answerFile) {
        try {
            Assignment assignment = assignmentRepository.findById(assignmentId)
                    .orElseThrow(() -> new RuntimeException("Assignment not found"));

            Blob answerBlob = new javax.sql.rowset.serial.SerialBlob(answerFile.getBytes());
            assignment.setStudentAnswerFile(answerBlob);
            assignment.setSubmittedByUserId(userId);
            assignmentRepository.save(assignment);
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Error while uploading assignment answer", e);
        }
    }

    @Override
    public Optional<User> findByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken); // Implemented method
    }
    
    @Override
    public List<Course> getEnrolledCoursesByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new ArrayList<>(user.getEnrolledCourses()); // Fetch enrolled courses
        }
        return Collections.emptyList(); // Return empty list if no courses are found
    }

   
    
}
