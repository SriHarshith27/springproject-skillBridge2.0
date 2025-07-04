package com.harshith.controller;

import com.harshith.dto.ApiResponse;
import com.harshith.dto.CourseDto;
import com.harshith.dto.CourseRequest;
import com.harshith.model.User;
import com.harshith.service.CourseService;
import com.harshith.service.DtoMapperService;
import com.harshith.validation.InputValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final DtoMapperService dtoMapper;
    private final InputValidator inputValidator;

    // --- PUBLIC ENDPOINTS ---

    @GetMapping
    public ResponseEntity<Page<CourseDto>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String category) {
        
        // Validate pagination parameters
        if (page < 0) page = 0;
        if (size < 1 || size > 100) size = 10; // Limit max page size
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Course> coursePage = courseService.getAllCourses(pageable);
        
        Page<CourseDto> courseDtoPage = coursePage.map(dtoMapper::toCourseDto);
        return ResponseEntity.ok(courseDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        try {
            CourseDto course = dtoMapper.toCourseDto(courseService.getCourseById(id));
            return ResponseEntity.ok(course);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- USER ENDPOINTS ---

    @PostMapping("/{courseId}/enroll")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> enrollInCourse(
            @PathVariable Long courseId, 
            @AuthenticationPrincipal User user) {
        try {
            courseService.enrollUserInCourse(user.getId(), courseId);
            return ResponseEntity.ok(new ApiResponse("Successfully enrolled in course."));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/assignments/{assignmentId}/submit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> submitAssignment(
            @PathVariable Long assignmentId,
            @RequestParam("file") MultipartFile answerFile,
            @AuthenticationPrincipal User user) {
        try {
            courseService.uploadStudentAssignment(assignmentId, user.getId(), answerFile);
            return ResponseEntity.ok(new ApiResponse("Assignment submitted successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(e.getMessage()));
        }
    }

    // --- MENTOR ENDPOINTS ---

    @PostMapping
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<CourseDto> createCourse(
            @Valid @RequestBody CourseRequest courseRequest, 
            @AuthenticationPrincipal User mentor) {
        try {
            CourseDto newCourse = dtoMapper.toCourseDto(courseService.createCourse(courseRequest, mentor));
            return ResponseEntity.status(HttpStatus.CREATED).body(newCourse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{courseId}/modules")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<ApiResponse> addModule(
            @PathVariable Long courseId,
            @RequestParam("title") String title,
            @RequestParam("duration") int duration,
            @RequestParam("video") MultipartFile videoFile,
            @AuthenticationPrincipal User mentor) {
        try {
            courseService.addModuleToCourse(courseId, title, videoFile, duration, mentor);
            return ResponseEntity.ok(new ApiResponse("Module added successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(e.getMessage()));
        }
    }

    @PostMapping("/{courseId}/assignments")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<ApiResponse> addAssignment(
            @PathVariable Long courseId,
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile assignmentFile,
            @AuthenticationPrincipal User mentor) {
        try {
            courseService.addAssignmentToCourse(courseId, title, assignmentFile, mentor);
            return ResponseEntity.ok(new ApiResponse("Assignment added successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(e.getMessage()));
        }
    }

    @PostMapping("/assignments/{assignmentId}/grade")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<ApiResponse> gradeAssignment(
            @PathVariable Long assignmentId,
            @RequestParam("grade") int grade,
            @AuthenticationPrincipal User mentor) {
        try {
            courseService.gradeAssignment(assignmentId, grade, mentor);
            return ResponseEntity.ok(new ApiResponse("Grade submitted successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<ApiResponse> deleteCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal User mentor) {
        try {
            courseService.deleteCourse(courseId, mentor);
            return ResponseEntity.ok(new ApiResponse("Course deleted successfully."));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(e.getMessage()));
        }
    }
}