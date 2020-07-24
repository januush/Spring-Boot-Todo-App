package com.example.januush.todolistapp.model.projection;

import com.example.januush.todolistapp.model.Project;
import com.example.januush.todolistapp.model.ProjectStep;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;

/**
 * View Model for display purposes
 */
public class ProjectWriteModel {
    @NotBlank(message = "Project description must not be null")
    private String description;
    @Valid // not only project model must be validated but also every element of the list
    private List<ProjectStep> steps;

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<ProjectStep> getSteps() {
        return steps;
    }

    public void setSteps(final List<ProjectStep> steps) {
        this.steps = steps;
    }

    public Project toProject() {
        var result = new Project();
        result.setDescription(description);
        steps.forEach(step -> step.setProject(result));
        result.setSteps(new HashSet<>(steps));
        return result;
    }
}
