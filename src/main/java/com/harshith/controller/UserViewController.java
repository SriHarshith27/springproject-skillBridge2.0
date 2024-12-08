package com.harshith.controller;

import com.harshith.model.Course;
import com.harshith.model.CourseModule;
import com.harshith.model.Assignment;
import com.harshith.model.User;
import com.harshith.service.CourseService;
import com.harshith.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserViewController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/dashboard")
    public String showUserDashboard(HttpSession session, Model model) {
        // Log session attributes for debugging
        System.out.println("Role: " + session.getAttribute("role"));
        System.out.println("Username: " + session.getAttribute("username"));

        // Retrieve session attributes
        String role = (String) session.getAttribute("role");
        String username = (String) session.getAttribute("username");

        // Check for role and username
        if (role == null || !"USER".equalsIgnoreCase(role)) { // Adjust condition to match "USER"
            return "redirect:/auth/login"; // Redirect if role is missing or invalid
        }

        if (username == null) {
            return "redirect:/auth/login"; // Redirect if username is missing
        }

        // Add username to the model for display
        model.addAttribute("username", username);
        return "user/user-dashboard"; // Render the dashboard
    }


    @GetMapping("/course-progress/{courseId}")
    public String showCourseProgress(@PathVariable Long courseId, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/auth/login"; // Redirect to login if not authenticated
        }

        // Fetch course details
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return "redirect:/user/enrolled-courses"; // Redirect if course not found
        }

        // Fetch all assignments for the course
        List<Assignment> assignments = courseService.getAssignmentsByCourseId(courseId);
        
        List<Course> enrolledCourses = courseService.getEnrolledCoursesByUserId(userId); // Fetch enrolled courses


        // Fetch course modules (video sessions) for the course
        List<CourseModule> modules = courseService.getModulesByCourseId(courseId);
        if (modules != null) {
            System.out.println("Modules retrieved for course ID: " + courseId + ", Count: " + modules.size());
        } else {
            System.out.println("No modules found for course ID: " + courseId);
        }

        // Add data to model
        model.addAttribute("enrolledCourses", enrolledCourses);
        model.addAttribute("course", course);
        model.addAttribute("assignments", assignments);
        model.addAttribute("modules", modules);

        return "user/course-progress"; // Render the course-progress.jsp page
    }

    
    @PostMapping("/de-enroll-course/{courseId}")
    public String deEnrollCourse(@PathVariable Long courseId, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Long userId = (Long) session.getAttribute("userId"); 
            if (userId == null) {
                return "redirect:/auth/login"; // Redirect if user is not logged in
            }

            
         // Remove assignments and grades for the user in this course
            
            courseService.removeAssignmentsAndGrades(userId, courseId);
            
            // Use the service to de-enroll the user from the course
            courseService.deEnrollStudentFromCourse(userId, courseId);
            redirectAttributes.addFlashAttribute("successMessage", "You have successfully de-enrolled from the course.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while de-enrolling. Please try again.");
            e.printStackTrace();
        }
        return "redirect:/courses/enrolled-courses"; 
    }

    
    
    @GetMapping("/assignments/course/{courseId}")
    public ResponseEntity<List<Assignment>> getAllAssignmentsForCourse(@PathVariable Long courseId) {
        List<Assignment> assignments = courseService.getAllAssignmentsForCourse(courseId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/assignments/student/{courseId}")
    public ResponseEntity<List<Assignment>> getAssignmentsForStudent(
            @PathVariable Long courseId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Assignment> studentAssignments = courseService.getAssignmentsForStudentByCourse(userId, courseId);
        return ResponseEntity.ok(studentAssignments);
    }
 

    @GetMapping("/video-session/{moduleId}")
    public ResponseEntity<byte[]> streamVideoSession(@PathVariable Long moduleId) {
        try {
            byte[] video = courseService.getSessionVideo(moduleId);
            if (video == null) {
                System.out.println("No video found for module ID: " + moduleId);
                return ResponseEntity.notFound().build();
            }
            System.out.println("Video found for module ID: " + moduleId);
            return ResponseEntity.ok()
                    .header("Content-Type", "video/mp4")
                    .header("Accept-Ranges", "bytes")
                    .body(video);
        } catch (Exception e) {
            System.out.println("Error streaming video for module ID: " + moduleId);
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }


    // Download Assignment
    @GetMapping("/download-assignment/{assignmentId}")
    public ResponseEntity<byte[]> downloadAssignment(@PathVariable Long assignmentId) {
        byte[] fileData = courseService.getAssignmentFile(assignmentId);

        if (fileData == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=assignment-" + assignmentId + ".pdf")
                .body(fileData);
    }
    
    @GetMapping("/user-help-center")
    public String showUserHelpCenter() {
        return "user/user-help-center"; // Maps to /WEB-INF/views/user/user-help-center.jsp
    }
    
    
    
    
    @PostMapping("/upload-assignment")
    public String uploadAssignmentAnswer(
            @RequestParam("assignmentId") Long assignmentId,
            @RequestParam("answerFile") MultipartFile answerFile,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login"; // Redirect to login if the user is not logged in
        }

        try {
            courseService.uploadStudentAssignment(assignmentId, userId, answerFile);
            redirectAttributes.addFlashAttribute("successMessage", "Assignment uploaded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload assignment. Please try again.");
            e.printStackTrace();
        }

        return "redirect:/courses/enrolled-courses";
    }


}
