package com.harshith.repository;

import com.harshith.model.Course;
import com.harshith.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    
    // Find non-deleted courses
    Page<Course> findByDeletedFalse(Pageable pageable);
    
    Optional<Course> findByIdAndDeletedFalse(Long id);
    
    List<Course> findByMentorAndDeletedFalse(User mentor);
    
    List<Course> findByMentorIdAndDeletedFalse(Long mentorId);
    
    List<Course> findByCategoryAndDeletedFalse(String category);
    
    List<Course> findByNameContainingIgnoreCaseAndDeletedFalse(String name);

    @Query("SELECT new map(c.name as courseName, COUNT(e.id) as enrolledCount) " +
           "FROM Course c JOIN c.enrolledUsers e WHERE c.deleted = false GROUP BY c.id")
    List<Map<String, Object>> getEnrollmentStats();
    
    // For backward compatibility
    default List<Course> findByMentor(User mentor) {
        return findByMentorAndDeletedFalse(mentor);
    }
    
    default List<Course> findByMentorId(Long mentorId) {
        return findByMentorIdAndDeletedFalse(mentorId);
    }
    
    default List<Course> findByCategory(String category) {
        return findByCategoryAndDeletedFalse(category);
    }
    
    default List<Course> findByNameContainingIgnoreCase(String name) {
        return findByNameContainingIgnoreCaseAndDeletedFalse(name);
    }
    
    @Override
    default Optional<Course> findById(Long id) {
        return findByIdAndDeletedFalse(id);
    }
}