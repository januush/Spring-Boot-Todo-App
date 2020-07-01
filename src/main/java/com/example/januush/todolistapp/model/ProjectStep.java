package com.example.januush.todolistapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "project_steps")
public class ProjectStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Project description must not be null")
    private String description;
    private int daysToDeadline;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getDaysToDeadline() {
        return daysToDeadline;
    }

    Project getProject() {
        return project;
    }
}
