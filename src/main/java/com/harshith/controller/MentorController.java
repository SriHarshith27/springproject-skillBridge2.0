package com.harshith.controller;

import java.io.IOException;  
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.harshith.model.Assignment;
import com.harshith.model.Course;
import com.harshith.model.CourseModule;
import com.harshith.model.User;
import com.harshith.repository.UserRepository;
import com.harshith.service.CourseService;
import com.harshith.service.UserService;

import org.springframework.http.MediaType;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/mentor")
public class MentorController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserRepository userRepository;
 
    	
    @Autowired
    private UserService userService;

    // Dashboard for Mentor
    @GetMapping("/dashboard")
    public String showMentorDashboard(HttpSession session, Model model) {
        String role = (String) session.getAttribute("role");

        if (role == null || !"MENTOR".equalsIgnoreCase(role)) {
            return "redirect:/auth/login";
        }

        model.addAttribute("username", session.getAttribute("username"));
        return "mentor/mentor-dashboard";
    }

    // Create Course Page
    @GetMapping("/create-course")
    public String showCreateCourseForm(HttpSession session, Model model) {
        String role = (String) session.getAttribute("role");

        if (role == null || !"MENTOR".equalsIgnoreCase(role)) {
            return "redirect:/auth/login";
        }

        return "mentor/create-course";
    }

 // Help Center
    @GetMapping("/mentor-help-center")
    public String showMentorHelpCenter(HttpSession session, Model model) {
        String role = (String) session.getAttribute("role");

        if (role == null || !"MENTOR".equalsIgnoreCase(role)) {
            return "redirect:/auth/login"; // Redirect to login if not authenticated
        }

        model.addAttribute("username", session.getAttribute("username"));
        return "mentor/mentor-help-center"; // Maps to WEB-INF/views/mentor/mentor-help-center.jsp
    }

    // Handle Course Creation
    @PostMapping("/create-course")
    public String createCourse(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam int duration,
            HttpSession session) {

        String username = (String) session.getAttribute("username");
        User mentor = userService.findByUsername(username).orElse(null);

        if (mentor != null && "MENTOR".equalsIgnoreCase(mentor.getRole())) {
            Course course = new Course();
            course.setName(name);
            course.setDescription(description);
            course.setDuration(duration);
            course.setMentor(mentor);

            courseService.saveCourse(course);
        }

        return "redirect:/courses";
    }

    @GetMapping("/manage-courses")
    public String showManageCoursesPage(HttpSession session, Model model) {
        Long mentorId = (Long) session.getAttribute("userId");

        // Validate user session and role
        String role = (String) session.getAttribute("role");
        if (mentorId == null || role == null || !"MENTOR".equalsIgnoreCase(role)) {
            return "redirect:/auth/login";
        }

        List<Course> courses = courseService.getCoursesByMentorId(mentorId);
        model.addAttribute("courses", courses);
        return "mentor/manage-courses";
    }
    
    
    @GetMapping("/manage-course/{courseId}")
    public String showCourseManagementPage(@PathVariable Long courseId, Model model) {
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return "redirect:/mentor/manage-courses";
        }

        // Fetch all assignments for the course
        List<Assignment> assignments = courseService.getAssignmentsByCourseId(courseId);

        // Fetch submitted assignments for grading
        List<Assignment> submittedAssignments = courseService.getSubmittedAssignmentsByCourse(courseId);

        // Fetch course modules (video sessions)
        List<CourseModule> modules = courseService.getModulesByCourseId(courseId);

        model.addAttribute("course", course);
        model.addAttribute("assignments", assignments); // Assignments for management
        model.addAttribute("submittedAssignments", submittedAssignments); // Assignments for grading
        model.addAttribute("modules", modules);

        return "mentor/course-management";
    }




    @PostMapping("/uploadVideoSession")
    public String uploadVideoSession(
        @RequestParam("courseId") Long courseId, 
        @RequestParam("moduleTitle") String moduleTitle,
        @RequestParam("videoFile") MultipartFile videoFile, 
        @RequestParam("moduleDuration") int moduleDuration,
        Model model) {
        
        try {
            courseService.addModuleToCourse(courseId, moduleTitle, videoFile, moduleDuration);
            model.addAttribute("successMessage", "Video session uploaded successfully!");
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Failed to upload video session. Please try again.");
        }
        
        return "mentor/course-management";  // Returning to course-management page
    }
    
    @PostMapping("/deleteVideoSession/{moduleId}")
    public String deleteVideoSession(@PathVariable Long moduleId, RedirectAttributes redirectAttributes) {
        try {
            courseService.deleteVideoSession(moduleId);
            redirectAttributes.addFlashAttribute("successMessage", "Video session deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete video session. Please try again.");
            e.printStackTrace();
        }
        return "redirect:/mentor/manage-course/" + moduleId; // Replace with the correct courseId
    }

    
    @PostMapping("/uploadAssignment")
    public String uploadAssignment(
            @RequestParam(value = "courseId", required = false) Long courseId,
            @RequestParam(value = "assignmentTitle", required = false) String assignmentTitle,
            @RequestParam(value = "assignmentFile", required = false) MultipartFile assignmentFile,
            RedirectAttributes redirectAttributes) {

        System.out.println("courseId: " + courseId);
        System.out.println("assignmentTitle: " + assignmentTitle);
        System.out.println("assignmentFile: " + assignmentFile);

        if (courseId == null || assignmentTitle == null || assignmentFile == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid input data. Please check the form.");
            return "redirect:/mentor/manage-course/" + (courseId != null ? courseId : "");
        }

        try {
            courseService.addAssignmentToCourse(courseId, assignmentTitle, assignmentFile);
            redirectAttributes.addFlashAttribute("message", "Assignment uploaded successfully!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload assignment. Please try again.");
            e.printStackTrace();
        }

        return "redirect:/mentor/manage-course/" + courseId;
    }

    @PostMapping("/delete-assignment/{assignmentId}")
    public String deleteAssignment(@PathVariable Long assignmentId, RedirectAttributes redirectAttributes) {
        try {
            // Fetch the courseId from the assignment before deleting
            Assignment assignment = courseService.getAssignmentById(assignmentId); // Fetch assignment details
            Long courseId = assignment.getCourse().getId(); // Get the associated courseId

            // Perform the delete operation
            courseService.deleteAssignment(assignmentId);
            redirectAttributes.addFlashAttribute("successMessage", "Assignment deleted successfully!");

            // Redirect to the course-management page with the courseId
            return "redirect:/mentor/manage-course/" + courseId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete assignment. Please try again.");
            e.printStackTrace();
            return "redirect:/mentor/manage-courses"; // Fallback redirection
        }
    }

    @PostMapping("/edit-assignment/{assignmentId}")
    public String editAssignment(
            @PathVariable Long assignmentId,
            @RequestParam("assignmentFile") MultipartFile assignmentFile,
            RedirectAttributes redirectAttributes) {
        try {
            // Update the assignment in the database
            courseService.editAssignment(assignmentId, assignmentFile);

            // Retrieve the associated courseId from the assignment
            Assignment assignment = courseService.getAssignmentById(assignmentId);
            Long courseId = assignment.getCourse().getId();

            // Add a success message
            redirectAttributes.addFlashAttribute("successMessage", "Assignment updated successfully!");

            // Redirect to the course management page with the correct courseId
            return "redirect:/mentor/manage-course/" + courseId;
        } catch (Exception e) {
            // Add an error message and redirect back in case of failure
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update assignment. Please try again.");
            e.printStackTrace();

            // Redirect back to the course management page (fallback)
            return "redirect:/mentor/manage-courses";
        }
    }


    @GetMapping("/download/session/{moduleId}")
    public ResponseEntity<byte[]> downloadSession(@PathVariable Long moduleId) {
        byte[] sessionVideo = courseService.getSessionVideo(moduleId);

        if (sessionVideo == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=session-video.mp4")
                .body(sessionVideo);
    }
    
    
    @GetMapping("/download-assignment/{assignmentId}")
    public ResponseEntity<byte[]> downloadAssignment(@PathVariable Long assignmentId) {
        System.out.println("Download request for assignment ID: " + assignmentId);
        byte[] studentAnswerFile = courseService.getAssignmentFile(assignmentId); // Fetch file

        if (studentAnswerFile == null) {
            System.out.println("Student answer file not found for assignment ID: " + assignmentId);
            return ResponseEntity.notFound().build();
        }

        System.out.println("Student answer file successfully retrieved for assignment ID: " + assignmentId);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=submitted-assignment-" + assignmentId + ".pdf")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(studentAnswerFile);
    }
    
    
  
   
    @PostMapping("/grade-assignment")
    public String gradeAssignment(@RequestParam("assignmentId") Long assignmentId,
                                  @RequestParam("grade") int grade,
                                  @RequestParam("courseId") Long courseId,
                                  RedirectAttributes redirectAttributes) {
        try {
            courseService.gradeAssignment(assignmentId, grade);
            redirectAttributes.addFlashAttribute("successMessage", "Grade submitted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to submit grade. Please try again.");
            e.printStackTrace();
        }
        return "redirect:/mentor/manage-course/" + courseId; // Redirect to course management page
    }
        
    @PostMapping("/delete-course/{courseId}")
    public String deleteCourse(
            @PathVariable Long courseId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        // Retrieve mentor ID from session
        Long mentorId = (Long) session.getAttribute("userId");
        
        if (mentorId == null) {
            // Redirect to login if the user is not logged in
            return "redirect:/auth/login";
        }

        try {
            // Call the service method to delete the course
            boolean isDeleted = courseService.deleteCourseByMentor(courseId, mentorId);

            if (isDeleted) {
                // Add success message if the course is deleted successfully
                redirectAttributes.addFlashAttribute("successMessage", "Course deleted successfully.");
            } else {
                // Add error message if the mentor doesn't own the course or deletion failed
                redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to delete this course or it could not be deleted.");
            }
        } catch (Exception e) {
            // Add generic error message for exceptions
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while deleting the course.");
            e.printStackTrace(); // Log the exception for debugging
        }

        // Redirect back to the manage-courses page
        return "redirect:/mentor/manage-courses";
    }

}
