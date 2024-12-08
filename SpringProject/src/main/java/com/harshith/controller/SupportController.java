package com.harshith.controller;

import com.harshith.model.SupportMessage;
import com.harshith.service.SupportMessageService;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/support")
public class SupportController {

    @Autowired
    private SupportMessageService service;

    // Support form for mentors and users
    @GetMapping("/form")
    public String showSupportForm() {
        return "common/support-form"; // Adjust the path as needed
    }

    @PostMapping("/submit")
    public String submitSupportForm(@RequestParam String name,
                                    @RequestParam String subject,
                                    @RequestParam String message,
                                    HttpSession session,
                                    Model model) {
        // Retrieve the user's ID from the session
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            model.addAttribute("errorMessage", "You need to log in to submit a support ticket.");
            return "common/support-form"; // Redirect to the form with an error message
        }

        // Save the support message
        SupportMessage supportMessage = new SupportMessage();
        supportMessage.setName(name);
        supportMessage.setSubject(subject);
        supportMessage.setMessage(message);
        supportMessage.setUserId(userId); // Set the sender's ID

        service.saveSupportMessage(supportMessage);

        // Add success message to the model
        model.addAttribute("successMessage", "Your support ticket has been submitted successfully!");

        // Return the same view (support-form.jsp) with the success message
        return "common/support-form";
    }


    
    
 // Retrieve messages with replies for the logged-in user/mentor
    @GetMapping("/my-messages")
    public String viewUserMessages(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId"); // Replace with mentorId if needed
        if (userId == null) {
            return "redirect:/auth/login"; // Redirect if not logged in
        }

        List<SupportMessage> messages = service.getMessagesByUserId(userId);
        model.addAttribute("messages", messages);
        return "common/support-messages"; // Frontend template in the 'common' folder
    }
    
    
    
    
    // Admin view for messages
    @GetMapping("/admin/messages")
    public String showMessages(Model model) {
        model.addAttribute("messages", service.getAllMessages());
        return "admin/admin-messages"; // Adjust the path as needed
    }

    @PostMapping("/admin/reply/{id}")
    public String replyToMessage(@PathVariable Long id, @RequestParam String reply, Model model) {
        service.updateMessageReply(id, reply);
        model.addAttribute("successMessage", "Reply sent successfully!");
        return "redirect:/support/admin/messages";
    }
}
