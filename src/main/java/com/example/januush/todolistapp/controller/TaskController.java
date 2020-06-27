package com.example.januush.todolistapp.controller;

import com.example.januush.todolistapp.model.Task;
import com.example.januush.todolistapp.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
class TaskController {
	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
	private final TaskRepository repository;

	TaskController(final TaskRepository repository) {
		this.repository = repository;
	}

	@GetMapping(value = "/tasks",params = {"!sort", "!page", "!size"}) // called instead of findAll from Repository when no other params are specified
	public ResponseEntity<CollectionModel<Task>> readAllTasks() {
		List<Task> collection = repository.findAll();
		CollectionModel<Task> resources = new CollectionModel<>(collection);
		resources.add(linkTo(methodOn(TaskController.class).readAllTasks()).withSelfRel());
		logger.warn("Exposing all the tasks");
		return ResponseEntity.ok(resources);
	}

	@GetMapping("/tasks")
	ResponseEntity<List<Task>> readAllTasks(Pageable pageable) {
		logger.info("Custom pageable");
		return ResponseEntity.ok(repository.findAll(pageable).getContent());
	}

	@PutMapping("/tasks/{id}")
	ResponseEntity<?> updateTask(@RequestBody @Valid Task taskToUpdate) {
		repository.save(taskToUpdate);
		return ResponseEntity.noContent().build();
	}
}
