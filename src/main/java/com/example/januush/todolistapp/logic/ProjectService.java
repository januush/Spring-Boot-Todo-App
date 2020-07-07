package com.example.januush.todolistapp.logic;

import com.example.januush.todolistapp.TaskConfigurationProperties;
import com.example.januush.todolistapp.model.*;
import com.example.januush.todolistapp.model.projection.GroupReadModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private TaskGroupRepository taskGroupRepository;
    private ProjectRepository projectRepository;
    private TaskConfigurationProperties config;

    ProjectService(final TaskGroupRepository repository, final ProjectRepository projectRepository, final TaskConfigurationProperties config) {
        this.taskGroupRepository = repository;
        this.projectRepository = projectRepository;
        this.config = config;
    }

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project save(Project toSave) {
        return projectRepository.save(toSave);
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        TaskGroup result = projectRepository.findById(projectId)
                .map(project -> {
                    var targetGroup = new TaskGroup();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(project.getSteps().stream()
                        .map(projectStep -> new Task(
                                projectStep.getDescription(),
                                deadline.plusDays(projectStep.getDaysToDeadline()))
                        ).collect(Collectors.toSet())
                    );
                    return taskGroupRepository.save(targetGroup);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
        return new GroupReadModel(result);
    }
}
