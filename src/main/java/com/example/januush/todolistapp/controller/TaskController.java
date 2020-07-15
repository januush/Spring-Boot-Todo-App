package com.example.januush.todolistapp.controller;

import com.example.januush.todolistapp.model.Task;
import com.example.januush.todolistapp.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
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
	public ResponseEntity<List<Task>> readAllTasks() {
		logger.warn("Exposing all the tasks");
		return ResponseEntity.ok(repository.findAll());
	}

	@GetMapping("/tasks")
	ResponseEntity<List<Task>> readAllTasks(Pageable pageable) {
		logger.info("Custom pageable");
		return ResponseEntity.ok(repository.findAll(pageable).getContent());
	}

	@GetMapping("/tasks/{id}")
	ResponseEntity<Task> readOneTask(@PathVariable("id") int taskId) {
		logger.info("Exposing one task");
		return repository.findById(taskId)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/tasks/{id}")
	ResponseEntity<?> updateTask(@PathVariable("id") int taskId, @RequestBody @Valid Task taskToUpdate) {
		logger.info("Updating one task");
		if (!repository.existsById(taskId)) {
			return ResponseEntity.notFound().build();
		}
		repository.findById(taskId)
				.ifPresent(task -> {
					task.updateFrom(taskToUpdate);
					repository.save(task);
				});
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/tasks")
	ResponseEntity<Task> createTask(@RequestBody @Valid Task taskToAdd) throws URISyntaxException {
		logger.info("Creating new task");
		Task result = repository.save(taskToAdd);
		return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
	}

	@Transactional
	@PatchMapping("/tasks/{id}")
	public ResponseEntity<?> toggleTask (@PathVariable int id) {
		if (!repository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		repository.findById(id)
				.ifPresent(task -> task.setDone(!task.isDone()));
		return ResponseEntity.noContent().build();
	}
}
