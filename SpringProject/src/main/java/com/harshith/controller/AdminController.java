package com.harshith.controller;

import com.harshith.model.Course;
import com.harshith.model.SupportMessage;
import com.harshith.model.User;
import com.harshith.repository.CourseRepository;
import com.harshith.service.SupportMessageService;
import com.harshith.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private SupportMessageService supportMessageService;
    
    @Autowired
    private CourseRepository courseRepository; 

    @GetMapping("/dashboard")
    public String showAdminDashboard() {
        return "admin/admin-dashboard"; // Maps to /WEB-INF/views/admin/admin-dashboard.jsp
    }

    @GetMapping("/manage-users")
    public String showManageUsersPage(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/manage-users"; // Maps to /WEB-INF/views/admin/manage-users.jsp
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam Long userId, RedirectAttributes redirectAttributes) {
        Optional<User> optionalUser = userService.getUserById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if ("MENTOR".equalsIgnoreCase(user.getRole())) {
                // Remove mentor association from all courses
                List<Course> mentorCourses = courseRepository.findByMentor(user);
                mentorCourses.forEach(course -> {
                    course.setMentor(null); // Set mentor to null
                    courseRepository.save(course); // Update course in the database
                });
            }

            // Delete the user
            userService.deleteUser(userId);
            redirectAttributes.addFlashAttribute("successMessage", "User '" + user.getUsername() + "' deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
        }

        return "redirect:/admin/manage-users";
    }

    
    @GetMapping("/view-user")
    public String viewUserDetails(@RequestParam("userId") Long userId, Model model) {
        User user = userService.getUserById(userId).orElse(null);
        if (user == null) {
            return "redirect:/admin/manage-users"; // Redirect if user not found
        }

        model.addAttribute("username", user.getUsername());
        model.addAttribute("role", user.getRole());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("phone", user.getPhone());

        return "admin/user-profile-view"; // Maps to WEB-INF/views/admin/user-profile-view.jsp
    }
    
    
    @GetMapping("/manage-courses")
    public String manageCourses(Model model) {
        List<Course> courses = courseRepository.findAll(); // Fetch courses
        model.addAttribute("courses", courses);
        return "admin/admin-manage-courses";
    }

    @PostMapping("/delete-course/{id}")
    public String deleteCourse(@PathVariable Long id,
                               @RequestParam("reason") String reason,
                               RedirectAttributes redirectAttributes) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();

            // Remove associations with users
            course.getEnrolledUsers().forEach(user -> user.getEnrolledCourses().remove(course));
            course.getEnrolledUsers().clear();

            // Create a support message for the mentor
            SupportMessage message = new SupportMessage();
            message.setName("Admin");
            message.setSubject("Course Deletion: " + course.getName());
            message.setMessage(reason);
            message.setUserId(course.getMentor().getId());
            supportMessageService.saveSupportMessage(message);

            // Delete the course
            courseRepository.delete(course);

            // Add success message to redirect attributes
            redirectAttributes.addFlashAttribute("successMessage", "Course '" + course.getName() + "' deleted successfully. Mentor has been notified.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Course not found.");
        }

        return "redirect:/admin/manage-courses";
    }
    

} 
 