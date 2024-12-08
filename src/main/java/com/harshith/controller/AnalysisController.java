package com.harshith.controller;

import com.harshith.service.CourseService;
import com.harshith.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    // Admin Analysis: User stats and enrollment data
    @GetMapping("/admin/user-stats")
    public Map<String, Long> getAdminUserStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("mentors", userService.countByRole("MENTOR"));
        stats.put("users", userService.countByRole("USER"));
        return stats;
    }

    @GetMapping("/admin/enrolled-students")
    public List<Map<String, Object>> getAdminEnrolledStudents() {
        return courseService.getEnrollmentStats();
    }
}

