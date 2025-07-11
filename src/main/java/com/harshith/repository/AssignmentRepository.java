package com.harshith.repository;

import com.harshith.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.repository.query.Param; // Add this import


@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findBySubmittedByUserId(Long userId);

    // This is the corrected method name
    List<Assignment> findByCourseIdAndStudentAnswerFileUrlNotNull(Long courseId);

    List<Assignment> findByCourseId(Long courseId);

    @Modifying
    @Query("DELETE FROM Assignment a WHERE a.id = :assignmentId")
    void deleteByIdAndFlush(@Param("assignmentId") Long assignmentId);
}