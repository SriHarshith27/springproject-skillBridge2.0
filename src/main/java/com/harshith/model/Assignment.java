package com.harshith.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data // A shortcut for @Getter, @Setter, @ToString, @EqualsAndHashCode
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private String title;

    private String assignmentFileUrl;

    private String studentAnswerFileUrl;

    private Long submittedByUserId; // ID of the user who submitted the answer

    private Integer grade;
}
