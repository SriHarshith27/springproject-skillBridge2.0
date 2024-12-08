package com.harshith.repository;

import org.springframework.data.jpa.repository.JpaRepository; 
import com.harshith.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsername(String username);
    long countByRole(String role);
    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String resetToken);
}
 
 