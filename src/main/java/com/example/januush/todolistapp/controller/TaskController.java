package com.example.januush.todolistapp.controller;

import com.example.januush.todolistapp.model.Task;
import com.example.januush.todolistapp.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RepositoryRestController
class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    TaskController(final TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/tasks",params = {"!sort", "!page", "!size"}) // called instead of findAll from Repository when no other params are specified
    public @ResponseBody ResponseEntity<?> readAllTasks() {
        List<Task> tasks = repository.findAll();
        CollectionModel<Task> resources = new CollectionModel<>(tasks);
        resources.add(linkTo(methodOn(TaskController.class).readAllTasks()).withSelfRel());
        logger.warn("Exposing all the tasks");
        return ResponseEntity.ok(resources);
    }
}
