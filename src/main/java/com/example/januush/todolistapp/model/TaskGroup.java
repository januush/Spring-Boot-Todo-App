package com.example.januush.todolistapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "tasks")
public class TaskGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Task group's description must not be empty")
    private String description;
    private boolean done;
    @Embedded
    private Audit audit = new Audit(); // inject updatedOn and createdOn fields with @Embedded

    TaskGroup() {
        // Empty constructor used by Hibernate to create single Entity
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}