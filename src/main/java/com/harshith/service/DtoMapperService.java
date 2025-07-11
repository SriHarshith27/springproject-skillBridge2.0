// src/main/java/com/harshith/service/DtoMapperService.java

package com.harshith.service;

import com.harshith.dto.AssignmentDto;
import com.harshith.dto.CourseDto;
import com.harshith.dto.CourseModuleDto;
import com.harshith.dto.UserDto;
import com.harshith.model.Assignment;
import com.harshith.model.Course;
import com.harshith.model.CourseModule;
import com.harshith.model.User;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class DtoMapperService {

    // --- USER MAPPING ---
    public UserDto toUserDto(User user) {
        if (user == null) return null;
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        if (user.getRole() != null) {
            dto.setRole(user.getRole().name());
        }
        return dto;
    }

    // --- COURSE MAPPING (UPDATED) ---
    public CourseDto toCourseDto(Course course) {
        if (course == null) return null;
        CourseDto dto = new CourseDto();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setCategory(course.getCategory());
        dto.setDuration(course.getDuration());
        dto.setMentor(toUserDto(course.getMentor()));

        // FIX: Map the modules and assignments lists
        if (course.getModules() != null) {
            dto.setModules(course.getModules().stream().map(this::toCourseModuleDto).collect(Collectors.toList()));
        }
        if (course.getAssignments() != null) {
            dto.setAssignments(course.getAssignments().stream().map(this::toAssignmentDto).collect(Collectors.toList()));
        }

        return dto;
    }

    // --- NEW HELPER METHODS ---
    public CourseModuleDto toCourseModuleDto(CourseModule module) {
        if (module == null) return null;
        CourseModuleDto dto = new CourseModuleDto();
        dto.setId(module.getId());
        dto.setTitle(module.getTitle());
        dto.setDuration(module.getDuration());
        dto.setSessionVideoUrl(module.getSessionVideoUrl());
        return dto;
    }

    public AssignmentDto toAssignmentDto(Assignment assignment) {
        if (assignment == null) return null;
        AssignmentDto dto = new AssignmentDto();
        dto.setId(assignment.getId());
        dto.setTitle(assignment.getTitle());
        dto.setAssignmentFileUrl(assignment.getAssignmentFileUrl());
        dto.setStudentAnswerFileUrl(assignment.getStudentAnswerFileUrl());
        dto.setGrade(assignment.getGrade());
        return dto;
    }
}