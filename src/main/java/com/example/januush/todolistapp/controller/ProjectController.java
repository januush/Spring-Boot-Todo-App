package com.example.januush.todolistapp.controller;

import com.example.januush.todolistapp.logic.ProjectService;
import com.example.januush.todolistapp.model.Project;
import com.example.januush.todolistapp.model.ProjectStep;
import com.example.januush.todolistapp.model.projection.ProjectWriteModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/projects")
class ProjectController {
    private final ProjectService service;

    ProjectController(final ProjectService service) {
        this.service = service;
    }

    @GetMapping
    String showProjects(Model model) {
        model.addAttribute("project", new ProjectWriteModel());
        return "projects"; // When returning String, Spring is looking if there is any template
    }

    @PostMapping
    String addProject(@ModelAttribute("project") ProjectWriteModel currentProjectModel, Model model) {
        // after adding we must have new ProjectWriteModel
        service.save(currentProjectModel);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("message", "Dodano projekt!");
        return "projects";

    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel currentProjectModel) {
        currentProjectModel.getSteps().add(new ProjectStep());
        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects() {
        return service.readAll();
    }
}
