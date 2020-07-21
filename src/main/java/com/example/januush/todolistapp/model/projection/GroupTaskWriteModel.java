package com.example.januush.todolistapp.model.projection;

import com.example.januush.todolistapp.model.Task;
import com.example.januush.todolistapp.model.TaskGroup;

import java.time.LocalDateTime;

class GroupTaskWriteModel {
    private String description;
    private LocalDateTime deadline;

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Task toTask(final TaskGroup group) {
        return new Task(description, deadline, group);
    }
}
