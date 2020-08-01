package com.example.januush.todolistapp.controller;

import com.example.januush.todolistapp.model.ProjectStep;
import com.example.januush.todolistapp.model.projection.ProjectWriteModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/projects")
class ProjectController {
    @GetMapping
    String showProjects(Model model) {
        model.addAttribute("project", new ProjectWriteModel());
        return "projects"; // When returning String, Spring is looking if there is any template
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel currentProjectModel) {
        currentProjectModel.getSteps().add(new ProjectStep());
        return "projects";
    }
}
