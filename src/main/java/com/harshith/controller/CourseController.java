package com.harshith.controller;

import com.harshith.dto.ApiResponse;
import com.harshith.dto.CourseDto;
import com.harshith.dto.CourseRequest;
import com.harshith.model.User;
import com.harshith.service.CourseService;
import com.harshith.service.DtoMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final DtoMapperService dtoMapper; // A new service to map entities to DTOs

    // --- PUBLIC ENDPOINTS ---

    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses(@RequestParam(required = false) String category) {
        List<CourseDto> courses = courseService.getAllCourses().stream()
                .map(dtoMapper::toCourseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        CourseDto course = dtoMapper.toCourseDto(courseService.getCourseById(id));
        return ResponseEntity.ok(course);
    }

    // --- USER ENDPOINTS ---

    @PostMapping("/{courseId}/enroll")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> enrollInCourse(@PathVariable Long courseId, @AuthenticationPrincipal User user) {
        courseService.enrollUserInCourse(user.getId(), courseId);
        return ResponseEntity.ok(new ApiResponse("Successfully enrolled in course."));
    }

    @PostMapping("/assignments/{assignmentId}/submit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> submitAssignment(
            @PathVariable Long assignmentId,
            @RequestParam("file") MultipartFile answerFile,
            @AuthenticationPrincipal User user) {
        courseService.uploadStudentAssignment(assignmentId, user.getId(), answerFile);
        return ResponseEntity.ok(new ApiResponse("Assignment submitted successfully."));
    }

    // --- MENTOR ENDPOINTS ---

    @PostMapping
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseRequest courseRequest, @AuthenticationPrincipal User mentor) {
        CourseDto newCourse = dtoMapper.toCourseDto(courseService.createCourse(courseRequest, mentor));
        return ResponseEntity.status(HttpStatus.CREATED).body(newCourse);
    }

    @PostMapping("/{courseId}/modules")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<ApiResponse> addModule(
            @PathVariable Long courseId,
            @RequestParam("title") String title,
            @RequestParam("duration") int duration,
            @RequestParam("video") MultipartFile videoFile) {
        courseService.addModuleToCourse(courseId, title, videoFile, duration);
        return ResponseEntity.ok(new ApiResponse("Module added successfully."));
    }

    @PostMapping("/{courseId}/assignments")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<ApiResponse> addAssignment(
            @PathVariable Long courseId,
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile assignmentFile) {
        courseService.addAssignmentToCourse(courseId, title, assignmentFile);
        return ResponseEntity.ok(new ApiResponse("Assignment added successfully."));
    }

    @PostMapping("/assignments/{assignmentId}/grade")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<ApiResponse> gradeAssignment(
            @PathVariable Long assignmentId,
            @RequestParam("grade") int grade) {
        // You should add logic here to verify the mentor owns the course of this assignment
        courseService.gradeAssignment(assignmentId, grade);
        return ResponseEntity.ok(new ApiResponse("Grade submitted successfully."));
    }
}
