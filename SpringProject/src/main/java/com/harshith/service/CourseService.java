package com.harshith.service;

import com.harshith.model.Assignment;
import com.harshith.model.Course;
import com.harshith.model.CourseModule;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface CourseService {

    List<Course> getAllCourses();

    Course getCourseById(Long id);

    Course saveCourse(Course course);

    Course updateCourse(Long id, Course course);

    void deleteCourse(Long id);

    void enrollUserInCourse(Long userId, Long courseId);

    List<Course> getCoursesByMentorId(Long mentorId);

    List<Course> getEnrolledCoursesByUserId(Long userId);

    List<CourseModule> getModulesByCourseId(Long courseId);

    List<Assignment> getAssignmentsByCourseId(Long courseId);

    void addModuleToCourse(Long courseId, String moduleTitle, MultipartFile videoFile, int duration) throws IOException;
    
    void uploadStudentAssignment(Long assignmentId, Long userId, MultipartFile answerFile) throws IOException;

    List<Assignment> getSubmittedAssignmentsByCourse(Long courseId);

    void gradeAssignment(Long assignmentId, int grade);

    List<Assignment> getAssignmentsForStudent(Long userId);
    
    List<Assignment> getAllAssignmentsForCourse(Long courseId);

    List<Assignment> getAssignmentsForStudentByCourse(Long userId, Long courseId);
    
    void deleteVideoSession(Long moduleId);

    void deleteAssignment(Long assignmentId);

    void editAssignment(Long assignmentId, MultipartFile assignmentFile) throws IOException;
    
    Assignment getAssignmentById(Long assignmentId);

    void deEnrollStudentFromCourse(Long studentId, Long courseId);

    void removeAssignmentsAndGrades(Long userId, Long courseId);
    
    List<Map<String, Object>> getEnrollmentStats();
    
    
    List<Course> getCoursesByCategory(String category);


    List<Course> searchCoursesByName(String keyword);

    List<String> getAllCategories(); 

    
    boolean deleteCourseByMentor(Long courseId, Long mentorId);

    void addAssignmentToCourse(Long courseId, String assignmentTitle, MultipartFile assignmentFile) throws IOException;

    byte[] getSessionVideo(Long moduleId); // Retrieve session video as byte array

    byte[] getAssignmentFile(Long assignmentId); // Retrieve assignment file as byte array
}
