package com.harshith.repository;

import com.harshith.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findBySubmittedByUserId(Long userId);

    // This is the corrected method name
    List<Assignment> findByCourseIdAndStudentAnswerFileUrlNotNull(Long courseId);

    List<Assignment> findByCourseId(Long courseId);
}