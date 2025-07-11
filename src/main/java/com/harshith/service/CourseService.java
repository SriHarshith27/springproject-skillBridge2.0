package com.harshith.service;

import com.harshith.dto.CourseRequest; // Import the DTO
import com.harshith.dto.SubmissionDto;
import com.harshith.model.Course;
import com.harshith.model.CourseModule;
import com.harshith.model.User; // Import the User model
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CourseService {

    // --- Course Management ---
    Page<Course> getAllCourses(Pageable pageable);
    Course getCourseById(Long id);
    Course createCourse(CourseRequest courseRequest, User mentor); // New method
    Course updateCourse(Long id, CourseRequest courseRequest, User currentUser); // Updated to use DTO
    void deleteCourse(Long id, User currentUser);
    List<Course> getCoursesByMentorId(Long mentorId);
    List<Course> getCoursesByCategory(String category);
    List<Course> searchCoursesByName(String keyword);

    // --- Enrollment ---
    void enrollUserInCourse(Long userId, Long courseId);
    void deEnrollStudentFromCourse(Long studentId, Long courseId);
    List<Map<String, Object>> getEnrollmentStats();


    //course-deletion

    void deleteModule(Long moduleId, User currentUser);
    void deleteAssignment(Long assignmentId, User currentUser);

    // --- Module & Assignment Management ---
    void addModuleToCourse(Long courseId, String moduleTitle, MultipartFile videoFile, int duration, User currentUser);
    void addAssignmentToCourse(Long courseId, String assignmentTitle, MultipartFile assignmentFile, User currentUser);
    void uploadStudentAssignment(Long assignmentId, Long userId, MultipartFile answerFile);
    void gradeAssignment(Long assignmentId, int grade, User currentUser); // New method

    List<SubmissionDto> getSubmissionsForCourse(Long courseId, User currentUser);

}