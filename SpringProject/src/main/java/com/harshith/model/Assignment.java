package com.harshith.model;

import jakarta.persistence.*;
import java.sql.Blob;

@Entity
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private String title;

    @Lob
    private Blob assignmentFile; // Blob field for storing assignment files

    @Lob
    private Blob studentAnswerFile; // Blob field for storing student answers

    private Long submittedByUserId; // ID of the user who submitted the answer

    private Integer grade;	
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Blob getAssignmentFile() {
        return assignmentFile;
    }

    public void setAssignmentFile(Blob assignmentFile) {
        this.assignmentFile = assignmentFile;
    }

    public Blob getStudentAnswerFile() {
        return studentAnswerFile;
    }

    public void setStudentAnswerFile(Blob studentAnswerFile) {
        this.studentAnswerFile = studentAnswerFile;
    }

    public Long getSubmittedByUserId() {
        return submittedByUserId;
    }

    public void setSubmittedByUserId(Long submittedByUserId) {
        this.submittedByUserId = submittedByUserId;
    }

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}
}
