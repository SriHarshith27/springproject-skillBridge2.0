package com.harshith.service;

import com.harshith.dto.CourseRequest; // Import the DTO
import com.harshith.model.Course;
import com.harshith.model.CourseModule;
import com.harshith.model.User; // Import the User model
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CourseService {

    // --- Course Management ---
    List<Course> getAllCourses();
    Course getCourseById(Long id);
    Course createCourse(CourseRequest courseRequest, User mentor); // New method
    Course updateCourse(Long id, CourseRequest courseRequest); // Updated to use DTO
    void deleteCourse(Long id);
    List<Course> getCoursesByMentorId(Long mentorId);
    List<Course> getCoursesByCategory(String category);
    List<Course> searchCoursesByName(String keyword);

    // --- Enrollment ---
    void enrollUserInCourse(Long userId, Long courseId);
    void deEnrollStudentFromCourse(Long studentId, Long courseId);
    List<Map<String, Object>> getEnrollmentStats();

    // --- Module & Assignment Management ---
    void addModuleToCourse(Long courseId, String moduleTitle, MultipartFile videoFile, int duration);
    void addAssignmentToCourse(Long courseId, String assignmentTitle, MultipartFile assignmentFile);
    void uploadStudentAssignment(Long assignmentId, Long userId, MultipartFile answerFile);
    void gradeAssignment(Long assignmentId, int grade); // New method
}
