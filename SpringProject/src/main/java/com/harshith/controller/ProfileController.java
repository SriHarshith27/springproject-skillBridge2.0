package com.harshith.controller;

import com.harshith.model.User;
import com.harshith.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String showAdminProfile(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        
        if (username == null || !"ADMIN".equalsIgnoreCase(role)) {
            return "redirect:/auth/login";
        }

        User user = userService.findByUsername(username).orElse(null);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("role", user.getRole());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("phone", user.getPhone());

        return "admin/profile"; 
    }
    
    
    @GetMapping("/admin/edit-profile")
    public String editAdminProfile(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null || !"ADMIN".equalsIgnoreCase(role)) {
            return "redirect:/auth/login";
        }

        User user = userService.findByUsername(username).orElse(null);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("phone", user.getPhone());

        return "admin/edit-profile"; 
    }
 
    

    
    // User profile mapping
    @GetMapping("/user")
    public String showUserProfile(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null || !"USER".equalsIgnoreCase(role)) {
            return "redirect:/auth/login";
        }

        User user = userService.findByUsername(username).orElse(null);
        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("username", user.getUsername());
        model.addAttribute("role", user.getRole());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("phone", user.getPhone());

        return "user/UserProfile"; 
    }
    
    
    @GetMapping("/user/edit-profile")
    public String editUserProfile(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null || !"USER".equalsIgnoreCase(role)) { // Correct role check
            return "redirect:/auth/login";
        }

        User user = userService.findByUsername(username).orElse(null);
        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("phone", user.getPhone());

        return "user/edit-profile"; // Ensure this matches the correct JSP file location
    }

       
    
 // Mentor profile mapping
    @GetMapping("/mentor")
    public String showMentorProfile(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null || !"MENTOR".equalsIgnoreCase(role)) {
            return "redirect:/auth/login";
        }

        User user = userService.findByUsername(username).orElse(null);
        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("username", user.getUsername());
        model.addAttribute("role", user.getRole());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("phone", user.getPhone());

        return "mentor/mentor-profile"; 
    }
    
    @GetMapping("/mentor/edit-profile")
    public String editMentorProfile(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null || !"MENTOR".equalsIgnoreCase(role)) {
            return "redirect:/auth/login";
        }

        User user = userService.findByUsername(username).orElse(null);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("phone", user.getPhone());

        return "mentor/edit-profile"; 
    }
 
        
    @GetMapping("/edit")
    public String showEditProfilePage(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        User user = userService.findByUsername(username).orElse(null);
        
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        return "profile/edit-profile"; 
    }

    
    
    @PostMapping("/update")
    public String updateProfile(
            HttpSession session,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam String currentPassword,
            @RequestParam(required = false) String newPassword,
            RedirectAttributes redirectAttributes) {

        String sessionUsername = (String) session.getAttribute("username");
        User user = userService.findByUsername(sessionUsername).orElse(null);

        if (user == null || !userService.isPasswordValid(user, currentPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid current password.");
            return "redirect:/profile/" + user.getRole().toLowerCase() + "/edit-profile";
        }

        // Update fields only if they are provided
        if (username != null && !username.isEmpty() && !user.getUsername().equals(username)) {
            if (userService.isUsernameUnique(username)) {
                user.setUsername(username);
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Username is already taken.");
                return "redirect:/profile/" + user.getRole().toLowerCase() + "/edit-profile";
            }
        }

        if (email != null && !email.isEmpty()) {
            user.setEmail(email);
        }

        if (phone != null && !phone.isEmpty()) {
            user.setPhone(phone);
        }

        // Update password only if a new password is provided and it's different from the current one
        if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals(currentPassword)) {
            userService.updatePassword(user, newPassword);
        }

        // Save updated user
        userService.updateUser(user);
        session.setAttribute("username", user.getUsername());

        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully.");
        return "redirect:/profile/" + user.getRole().toLowerCase();
    }


}
