package com.harshith.dto;


import lombok.Data;

@Data
public class CourseModuleDto {
    private Long id;
    private String title;
    private String sessionVideoUrl;
    private int duration;
}