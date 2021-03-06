package com.example.januush.todolistapp.logic;

import com.example.januush.todolistapp.model.Project;
import com.example.januush.todolistapp.model.TaskGroup;
import com.example.januush.todolistapp.model.TaskGroupRepository;
import com.example.januush.todolistapp.model.TaskRepository;
import com.example.januush.todolistapp.model.projection.GroupReadModel;
import com.example.januush.todolistapp.model.projection.GroupWriteModel;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service layer between repository and controller
 */
@org.springframework.stereotype.Service
@RequestScope
public class TaskGroupService {
    private TaskGroupRepository repository;
    private TaskRepository taskRepository;

    TaskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(final GroupWriteModel source) {
        return createGroup(source, null);
    }

    GroupReadModel createGroup(final GroupWriteModel source, final Project project) {
        TaskGroup result = repository.save(source.toGroup(project));
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll()
                .stream()
                .filter(Objects::nonNull)
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if (taskRepository.existsByDoneIsFalseAndGroupId(groupId)) {
            throw new IllegalStateException("Group has undone tasks.");
        }
        TaskGroup result = repository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }
}
