package com.example.januush.todolistapp.controller;

import com.example.januush.todolistapp.logic.TaskGroupService;
import com.example.januush.todolistapp.model.ProjectStep;
import com.example.januush.todolistapp.model.Task;
import com.example.januush.todolistapp.model.TaskRepository;
import com.example.januush.todolistapp.model.projection.GroupReadModel;
import com.example.januush.todolistapp.model.projection.GroupTaskWriteModel;
import com.example.januush.todolistapp.model.projection.GroupWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/groups")
class TaskGroupController {
	private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
	private final TaskGroupService taskGroupService;
	private final TaskRepository taskRepository;

	TaskGroupController(final TaskGroupService taskGroupService, final TaskRepository taskRepository) {
		this.taskGroupService = taskGroupService;
		this.taskRepository = taskRepository;
	}

	@GetMapping(produces = MediaType.TEXT_HTML_VALUE)
	String showGroups(Model model) {
		model.addAttribute("group", new GroupWriteModel());
		return "groups";
	}

	@PostMapping(produces = MediaType.TEXT_HTML_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	String addGroup(
			@ModelAttribute("group") @Valid GroupWriteModel currentGroupWriteModel,
			BindingResult bindingResult,
			Model model
	) {
		if (bindingResult.hasErrors()) {
			return "groups";
		}

		taskGroupService.createGroup(currentGroupWriteModel);
		model.addAttribute("group", new GroupWriteModel());
		model.addAttribute("groups", getGroups());
		model.addAttribute("message", "Dodano grupę!");
		return "groups";

	}

	@ModelAttribute("groups")
	List<GroupReadModel> getGroups() {
		return taskGroupService.readAll();
	}

	@PostMapping(params = "addTask", produces = MediaType.TEXT_HTML_VALUE)
	String addGroupTask(@ModelAttribute("group") GroupWriteModel currentGroupWriteModel) {
		currentGroupWriteModel.getTasks().add(new GroupTaskWriteModel());
		return "groups";
	}

	@ResponseBody
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel source) {
		logger.info("Creating new task group");
		GroupReadModel result = taskGroupService.createGroup(source);
		return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
	}

	@ResponseBody
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<List<GroupReadModel>> readAllGroups() {
		return ResponseEntity.ok(taskGroupService.readAll());
	}

	@ResponseBody
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id) {
		return ResponseEntity.ok(taskRepository.findAllByGroup_Id(id));
	}

	@ResponseBody
	@Transactional
	@PatchMapping(value = "/{id}")
	public ResponseEntity<?> toggleGroup (@PathVariable int id) {
		taskGroupService.toggleGroup(id);
		return ResponseEntity.noContent().build();
	}
}
