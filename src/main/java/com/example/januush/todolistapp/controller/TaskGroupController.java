package com.example.januush.todolistapp.controller;

import com.example.januush.todolistapp.logic.TaskGroupService;
import com.example.januush.todolistapp.model.Task;
import com.example.januush.todolistapp.model.TaskRepository;
import com.example.januush.todolistapp.model.projection.GroupReadModel;
import com.example.januush.todolistapp.model.projection.GroupWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/groups")
class TaskGroupController {
	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
	private final TaskGroupService taskGroupService;
	private final TaskRepository taskRepository;

	TaskGroupController(final TaskGroupService taskGroupService, final TaskRepository taskRepository) {
		this.taskGroupService = taskGroupService;
		this.taskRepository = taskRepository;
	}

	@PostMapping
	ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel source) {
		logger.info("Creating new task group");
		GroupReadModel result = taskGroupService.createGroup(source);
		return ResponseEntity.created(URI.create("/" + result.getId())).body(taskGroupService.createGroup(source));
	}

	@GetMapping
	ResponseEntity<List<GroupReadModel>> readAllGroups() {
		return ResponseEntity.ok(taskGroupService.readAll());
	}

	@GetMapping("/{id}/tasks")
	ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id) {
		return ResponseEntity.ok(taskRepository.findAllByGroup_Id(id));
	}

	@Transactional
	@PatchMapping("/{id}")
	public ResponseEntity<?> toggleGroup (@PathVariable int id) {
		taskGroupService.toggleGroup(id);
		return ResponseEntity.noContent().build();
	}
}
