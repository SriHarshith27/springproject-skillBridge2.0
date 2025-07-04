package com.harshith.repository;

import com.harshith.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameAndDeletedFalse(String username);
    
    Optional<User> findByEmailAndDeletedFalse(String email);
    
    Optional<User> findByIdAndDeletedFalse(Long id);
    
    Page<User> findByDeletedFalse(Pageable pageable);

    long countByRoleAndDeletedFalse(String role);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.deleted = false")
    long countByRoleAndDeletedFalseCustom(@Param("role") String role);

    // For backward compatibility
    default Optional<User> findByUsername(String username) {
        return findByUsernameAndDeletedFalse(username);
    }
    
    default Optional<User> findByEmail(String email) {
        return findByEmailAndDeletedFalse(email);
    }
    
    default long countByRole(String role) {
        return countByRoleAndDeletedFalse(role);
    }
}