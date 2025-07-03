package com.harshith.dto;


import lombok.Data;

@Data
public class AssignmentDto {
    private Long id;
    private String title;
    private String assignmentFileUrl;
    private String studentAnswerFileUrl;
    private Long submittedByUserId;
    private Integer grade;
}