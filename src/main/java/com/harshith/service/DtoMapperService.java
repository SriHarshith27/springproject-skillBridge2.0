package com.harshith.service;

import com.harshith.dto.CourseDto;
import com.harshith.dto.UserDto;
import com.harshith.model.Course;
import com.harshith.model.User;
import org.springframework.stereotype.Service;

@Service
public class DtoMapperService {

    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());

        // THIS IS THE FIX: Convert the Role enum to a String using .name()
        if (user.getRole() != null) {
            dto.setRole(user.getRole().name());
        }

        return dto;
    }

    public CourseDto toCourseDto(Course course) {
        if (course == null) {
            return null;
        }
        CourseDto dto = new CourseDto();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setCategory(course.getCategory());
        dto.setDuration(course.getDuration());
        dto.setMentor(toUserDto(course.getMentor()));
        return dto;
    }
}
