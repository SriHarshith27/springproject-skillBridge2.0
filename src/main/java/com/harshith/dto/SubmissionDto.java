package com.harshith.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionDto {
    private Long assignmentId;
    private String assignmentTitle;
    private Long studentId;
    private String studentUsername;
    private String answerFileUrl;
    private Integer grade;
}