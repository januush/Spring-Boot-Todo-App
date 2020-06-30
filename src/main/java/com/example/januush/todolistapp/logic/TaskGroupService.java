package com.example.januush.todolistapp.logic;

import com.example.januush.todolistapp.model.Task;
import com.example.januush.todolistapp.model.TaskGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
class TaskGroupService {
    @Autowired
    List<String> temp(TaskGroupRepository repository) {
       return repository.findAll()
                .stream()
                .flatMap(taskGroup -> taskGroup.getTasks().stream())
                .map(Task::getDescription)
                .collect(Collectors.toList());
    }
}
