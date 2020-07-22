package com.example.januush.todolistapp.controller;

import com.example.januush.todolistapp.model.Task;
import com.example.januush.todolistapp.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/tasks")
class TaskController {
	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
	private final TaskRepository repository;

	TaskController(final TaskRepository repository) {
		this.repository = repository;
	}

	@GetMapping(params = {"!sort", "!page", "!size"}) // called instead of findAll from Repository when no other params are specified
	public ResponseEntity<List<Task>> readAllTasks() {
		logger.warn("Exposing all the tasks");
		return ResponseEntity.ok(repository.findAll());
	}

	@GetMapping
	ResponseEntity<List<Task>> readAllTasks(Pageable pageable) {
		logger.info("Custom pageable");
		return ResponseEntity.ok(repository.findAll(pageable).getContent());
	}

	@GetMapping("/{id}")
	ResponseEntity<Task> readOneTask(@PathVariable("id") int taskId) {
		logger.info("Exposing one task");
		return repository.findById(taskId)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
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

	@PostMapping
	ResponseEntity<Task> createTask(@RequestBody @Valid Task taskToAdd) throws URISyntaxException {
		logger.info("Creating new task");
		Task result = repository.save(taskToAdd);
		return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
	}

	@Transactional
	@PatchMapping("/{id}")
	public ResponseEntity<?> toggleTask (@PathVariable int id) {
		if (!repository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		repository.findById(id)
				.ifPresent(task -> task.setDone(!task.isDone()));
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/search/done")
	ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state) {
		return ResponseEntity.ok(
				repository.findByDone(state)
		);
	}
}
