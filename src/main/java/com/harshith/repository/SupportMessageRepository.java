package com.harshith.repository;

import com.harshith.model.SupportMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportMessageRepository extends JpaRepository<SupportMessage, Long> {

    // This method was missing. Spring Data JPA will automatically implement it.
    List<SupportMessage> findByUserId(Long userId);
}
