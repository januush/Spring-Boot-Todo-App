package com.example.januush.todolistapp.logic;

import com.example.januush.todolistapp.TaskConfigurationProperties;
import com.example.januush.todolistapp.model.*;
import com.example.januush.todolistapp.model.projection.GroupReadModel;
import com.example.januush.todolistapp.model.projection.GroupTaskWriteModel;
import com.example.januush.todolistapp.model.projection.GroupWriteModel;
import com.example.januush.todolistapp.model.projection.ProjectWriteModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private TaskGroupRepository taskGroupRepository;
    private ProjectRepository projectRepository;
    private TaskConfigurationProperties config;
    private TaskGroupService service;

    ProjectService(final TaskGroupRepository repository, final ProjectRepository projectRepository, final TaskConfigurationProperties config, final TaskGroupService service) {
        this.taskGroupRepository = repository;
        this.projectRepository = projectRepository;
        this.config = config;
        this.service = service;
    }

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project save(ProjectWriteModel toSave) {
        return projectRepository.save(toSave.toProject());
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        GroupReadModel result = projectRepository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(project.getSteps().stream()
                            .map(projectStep -> {
                                    var task = new GroupTaskWriteModel();
                                    task.setDescription(projectStep.getDescription());
                                    task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                    return task;
                                }
                            ).collect(Collectors.toSet())
                    );
                    return service.createGroup(targetGroup, project);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
        return result;
    }
}
