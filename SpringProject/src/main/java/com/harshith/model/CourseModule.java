package com.harshith.model;

import jakarta.persistence.*;
import java.sql.Blob;

@Entity
public class CourseModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Lob
    private Blob sessionVideo; // Blob field for storing session video

    private String title;

    private int duration;  // Duration in minutes

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

    public Blob getSessionVideo() {
        return sessionVideo;
    }

    public void setSessionVideo(Blob sessionVideo) {
        this.sessionVideo = sessionVideo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
