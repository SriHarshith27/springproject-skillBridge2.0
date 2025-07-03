package com.harshith.service;

import com.harshith.model.Course;
import com.harshith.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    User updateUser(Long id, User updatedUser);

    User registerUser(User user);

    void deleteUser(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> validateUser(String username, String password);

    boolean isPasswordValid(User user, String currentPassword);

    void updatePassword(User user, String newPassword);

    List<Course> getEnrolledCoursesByUserId(Long userId);

    long countByRole(String role);
}
