package com.harshith.controller;

import com.harshith.dto.ApiResponse;
import com.harshith.dto.UserDto;
import com.harshith.model.User;
import com.harshith.service.DtoMapperService;
import com.harshith.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final DtoMapperService dtoMapper;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers().stream()
                .map(dtoMapper::toUserDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse> deleteUser(
            @PathVariable Long id, 
            @AuthenticationPrincipal User currentUser) {
        userService.deleteUser(id, currentUser);
        return ResponseEntity.ok(new ApiResponse("User deleted successfully."));
    }
}