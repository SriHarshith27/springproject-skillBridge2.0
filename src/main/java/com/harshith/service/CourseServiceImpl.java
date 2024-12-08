package com.harshith.service;

import com.harshith.model.Assignment;
import com.harshith.model.Course;
import com.harshith.model.CourseModule;
import com.harshith.model.User;
import com.harshith.repository.AssignmentRepository;
import com.harshith.repository.CourseRepository;
import com.harshith.repository.UserRepository;

import jakarta.transaction.Transactional;

import com.harshith.repository.CourseModuleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseModuleRepository courseModuleRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId).orElse(null);
    }

    @Override
    public List<Course> getCoursesByMentorId(Long mentorId) {
        return courseRepository.findByMentorId(mentorId);
    }

    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }
    
    
    @Override
    public List<Map<String, Object>> getEnrollmentStats() {
        return courseRepository.getEnrollmentStats();
    }

    
    
    @Override
    public Course updateCourse(Long id, Course updatedCourse) {
        return courseRepository.findById(id).map(course -> {
            course.setName(updatedCourse.getName());
            course.setDescription(updatedCourse.getDescription());
            course.setDuration(updatedCourse.getDuration());
            return courseRepository.save(course);
        }).orElse(null);
    }

    @Override
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    public List<Course> getEnrolledCoursesByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new ArrayList<>(user.getEnrolledCourses()); // Convert Set<Course> to List<Course>
    }


    @Override
    public void enrollUserInCourse(Long userId, Long courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        user.getEnrolledCourses().add(course); // Updated to match the new field name
        userRepository.save(user); // Save changes to the user
    }

    @Override
    public void addModuleToCourse(Long courseId, String moduleTitle, MultipartFile videoFile, int duration) {
        try {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            CourseModule module = new CourseModule();
            module.setCourse(course);
            module.setTitle(moduleTitle);
            module.setDuration(duration);

            Blob videoBlob = new SerialBlob(videoFile.getBytes());
            module.setSessionVideo(videoBlob);

            course.addModule(module);
            courseRepository.save(course);
        } catch (Exception e) {
            throw new RuntimeException("Error adding module to course", e);
        }
    }

    @Override
    public List<CourseModule> getModulesByCourseId(Long courseId) {
        List<CourseModule> modules = courseModuleRepository.findByCourseId(courseId);
        if (modules != null && !modules.isEmpty()) {
            System.out.println("Modules retrieved successfully: " + modules.size());
        } else {
            System.out.println("No modules found for course ID: " + courseId);
        }
        return modules;
    }

    @Override
    @Transactional
    public void deEnrollStudentFromCourse(Long studentId, Long courseId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Remove the course from the student's enrolled courses
        student.getEnrolledCourses().remove(course);

        // Remove the student from the course's enrolled users
        course.getEnrolledUsers().remove(student);

        // Save both entities to persist changes
        userRepository.save(student);
        courseRepository.save(course);
    }
   
    
    @Override
    public boolean deleteCourseByMentor(Long courseId, Long mentorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Check if the course belongs to the mentor
        if (!course.getMentor().getId().equals(mentorId)) {
            return false; // Return false if the mentor doesn't own the course
        }

        try {
            // Remove associations between users and the course
            Set<User> enrolledUsers = course.getEnrolledUsers(); // Use Set<User> as defined in the updated Course entity
            for (User user : enrolledUsers) {
                user.getEnrolledCourses().remove(course); // Updated to match the new field name in User entity
                userRepository.save(user); // Save updated user to persist the changes
            }

            // Delete the course itself
            courseRepository.delete(course);
            return true; // Return true on successful deletion
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting the course and its dependencies", e);
        }
    }

    
    @Override
    public List<Assignment> getAssignmentsByCourseId(Long courseId) {
        List<Assignment> assignments = assignmentRepository.findByCourseId(courseId);
        if (assignments != null && !assignments.isEmpty()) {
            System.out.println("Assignments retrieved successfully for Course ID: " + courseId);
        } else {
            System.out.println("No assignments found for Course ID: " + courseId);
        }
        return assignments;
    }



    @Override
    public Assignment getAssignmentById(Long assignmentId) {
        return assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with ID: " + assignmentId));
    }

    
    @Override
    public void addAssignmentToCourse(Long courseId, String assignmentTitle, MultipartFile assignmentFile) {
        try {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found for ID: " + courseId));

            System.out.println("Adding assignment to course: " + course.getName());

            Assignment assignment = new Assignment();
            assignment.setCourse(course);
            assignment.setTitle(assignmentTitle);

            if (!assignmentFile.isEmpty()) {
                Blob fileBlob = new SerialBlob(assignmentFile.getBytes());
                assignment.setAssignmentFile(fileBlob);
                System.out.println("Assignment file uploaded successfully.");
            }

            course.addAssignment(assignment);
            courseRepository.save(course);
            System.out.println("Assignment added and saved successfully.");
        } catch (IOException e) {
            throw new RuntimeException("Error while processing assignment file", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while adding assignment to course", e);
        }
    }


 

    @Override
    public void gradeAssignment(Long assignmentId, int grade) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        assignment.setGrade(grade); // Set the grade
        assignmentRepository.save(assignment); // Save the updated assignment
    }

    @Override
    public List<Assignment> getAssignmentsForStudent(Long userId) {
        return assignmentRepository.findBySubmittedByUserId(userId); // Fetch all assignments submitted by the student
    }

    @Override
    public List<Assignment> getSubmittedAssignmentsByCourse(Long courseId) {
        List<Assignment> submittedAssignments = assignmentRepository.findByCourseIdAndStudentAnswerFileNotNull(courseId);
        if (submittedAssignments != null && !submittedAssignments.isEmpty()) {
            System.out.println("Submitted assignments retrieved successfully for Course ID: " + courseId);
        } else {
            System.out.println("No submitted assignments found for Course ID: " + courseId);
        }
        return submittedAssignments;
    }

    
    @Override
    public void uploadStudentAssignment(Long assignmentId, Long userId, MultipartFile answerFile) {
        try {
            Assignment assignment = assignmentRepository.findById(assignmentId)
                    .orElseThrow(() -> new RuntimeException("Assignment not found"));

            Blob answerBlob = new javax.sql.rowset.serial.SerialBlob(answerFile.getBytes());
            assignment.setStudentAnswerFile(answerBlob);
            assignment.setSubmittedByUserId(userId);
            assignmentRepository.save(assignment);
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Error while uploading assignment answer", e);
        }
    }
    
    
    @Override
    public List<Assignment> getAllAssignmentsForCourse(Long courseId) {
        return assignmentRepository.findByCourseId(courseId); // Fetch all assignments for the course
    }

    	
    @Override
    public List<Assignment> getAssignmentsForStudentByCourse(Long userId, Long courseId) {
        List<Assignment> allAssignments = assignmentRepository.findByCourseId(courseId); // Fetch all assignments for the course

        // Filter assignments where `submittedByUserId` matches the user
        List<Assignment> studentAssignments = new ArrayList<>();
        for (Assignment assignment : allAssignments) {
            if (assignment.getSubmittedByUserId() != null && assignment.getSubmittedByUserId().equals(userId)) {
                studentAssignments.add(assignment);
            }
        }
        return studentAssignments;
    }

    
    @Override
    @Transactional
    public void removeAssignmentsAndGrades(Long userId, Long courseId) {
        // Find all assignments submitted by the user for this course
        List<Assignment> assignments = assignmentRepository.findByCourseId(courseId);

        for (Assignment assignment : assignments) {
            // Remove only those assignments submitted by the user
            if (userId.equals(assignment.getSubmittedByUserId())) {
                assignment.setStudentAnswerFile(null); // Nullify any submitted file
                assignment.setSubmittedByUserId(null); // Remove user association
                assignment.setGrade(null); // Reset grade
                assignmentRepository.save(assignment); // Save changes to the assignment
            }
        }

        System.out.println("All assignments and grades for user " + userId + " in course " + courseId + " have been removed.");
    }

    
    
    @Override
    public byte[] getSessionVideo(Long moduleId) {
        CourseModule module = courseModuleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found with ID: " + moduleId));

        try {
            if (module.getSessionVideo() != null) {
                System.out.println("Video retrieved for Module ID: " + moduleId);
                return module.getSessionVideo().getBytes(1, (int) module.getSessionVideo().length());
            } else {
                System.out.println("No video found for Module ID: " + moduleId);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error retrieving video for Module ID: " + moduleId);
            e.printStackTrace();
            return null;
        }
    }


    
   
    @Override
    public byte[] getAssignmentFile(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with ID: " + assignmentId));

        try {
            // Fetch the assignment file
            if (assignment.getAssignmentFile() != null) {
                System.out.println("Assignment file retrieved for assignment ID: " + assignmentId);
                return assignment.getAssignmentFile().getBytes(1, (int) assignment.getAssignmentFile().length());
            } else {
                System.out.println("No assignment file found for assignment ID: " + assignmentId);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error retrieving assignment file for assignment ID: " + assignmentId);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteVideoSession(Long moduleId) {
        CourseModule module = courseModuleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found with ID: " + moduleId));
        courseModuleRepository.delete(module);
    }

    @Override
    @Transactional
    public void deleteAssignment(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with ID: " + assignmentId));
        assignmentRepository.delete(assignment);
    }
    
    @Override
    @Transactional
    public void editAssignment(Long assignmentId, MultipartFile assignmentFile) {
        try {
            Assignment assignment = assignmentRepository.findById(assignmentId)
                    .orElseThrow(() -> new RuntimeException("Assignment not found with ID: " + assignmentId));

            Blob fileBlob = new SerialBlob(assignmentFile.getBytes());
            assignment.setAssignmentFile(fileBlob);
            assignmentRepository.save(assignment);
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Error while updating assignment", e);
        }
   
    }
    
    @Override
    public List<String> getAllCategories() {
        // Retrieve distinct categories from the database
        return courseRepository.findAll()
                .stream()
                .map(Course::getCategory) // Assuming "category" is a field in the Course entity
                .distinct()
                .collect(Collectors.toList());
    }

   
    
    @Override
    public List<Course> getCoursesByCategory(String category) {
        return courseRepository.findByCategory(category);
    }

    @Override
    public List<Course> searchCoursesByName(String keyword) {
        return courseRepository.findByNameContainingIgnoreCase(keyword);
    }
  }


 