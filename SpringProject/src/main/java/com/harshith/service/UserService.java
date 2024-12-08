package com.harshith.service;

import com.harshith.model.Assignment;
import com.harshith.model.Course;
import com.harshith.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    List<User> getAllUsers();

    User updateUser(Long id, User updatedUser);

    boolean isUsernameUnique(String username);

 
    User registerUser(User user);

    void deleteUser(Long id);

    Optional<User> getUserById(Long id);
    Optional<User> findByResetToken(String resetToken); // Add this method


    Optional<User> findByUsername(String username);


    Optional<User> validateUser(String username, String password);
    
    boolean isPasswordValid(User user, String currentPassword);

    void updatePassword(User user, String newPassword);
    
    void uploadStudentAssignment(Long assignmentId, Long userId, MultipartFile answerFile) throws IOException;
    
    List<Assignment> getAssignmentsByUserId(Long userId);
    
    List<Course> getEnrolledCoursesByUserId(Long userId);



    void updateUser(User user);
    
    long countByRole(String role);

    
}
