package com.harshith.repository;

import com.harshith.model.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseModuleRepository extends JpaRepository<CourseModule, Long> {

    @Query("SELECT cm FROM CourseModule cm WHERE cm.course.id = :courseId")
    List<CourseModule> findByCourseId(@Param("courseId") Long courseId);
}
