package com.harshith.controller;

import com.harshith.dto.ApiResponse;
import com.harshith.dto.PasswordChangeRequest;
import com.harshith.model.Course;
import com.harshith.model.User;
import com.harshith.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.harshith.dto.CourseDto;
import com.harshith.service.DtoMapperService;
import org.springframework.web.bind.annotation.*; // Import GetMapping
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    private final DtoMapperService dtoMapperService;

    @GetMapping("/my-courses")
    public ResponseEntity<List<CourseDto>> getMyEnrolledCourses(@AuthenticationPrincipal User currentUser) {
        List<Course> enrolledCourses = userService.getEnrolledCoursesByUserId(currentUser.getId());

        List<CourseDto> courseDtos = enrolledCourses.stream()
                .map(dtoMapperService::toCourseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(courseDtos);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody PasswordChangeRequest request) {

        // First, check if the provided current password is valid
        if (!userService.isPasswordValid(currentUser, request.getCurrentPassword())) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid current password."));
        }

        // If valid, update to the new password
        userService.updatePassword(currentUser, request.getNewPassword());

        return ResponseEntity.ok(new ApiResponse("Password changed successfully."));
    }
}