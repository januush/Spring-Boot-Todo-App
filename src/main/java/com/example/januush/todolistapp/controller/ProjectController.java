package com.example.januush.todolistapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/projects")
class ProjectController {
    @GetMapping
    String showProjects() {
        return "projects"; // When returning String, Spring is looking if there is any template
    }
}
