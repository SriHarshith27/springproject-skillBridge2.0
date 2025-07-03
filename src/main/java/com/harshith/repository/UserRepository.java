package com.harshith.repository;

import com.harshith.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Changed from List<User> to Optional<User> for correctness, as username is unique.
    Optional<User> findByUsername(String username);

    // findByEmail is correct as email is also unique.
    Optional<User> findByEmail(String email);

    long countByRole(String role);

    // The findByResetToken method is no longer needed and has been removed.
}
