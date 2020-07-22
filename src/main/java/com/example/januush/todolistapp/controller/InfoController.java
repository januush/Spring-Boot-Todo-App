package com.example.januush.todolistapp.controller;

import com.example.januush.todolistapp.TaskConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/info")
class InfoController {
    private DataSourceProperties dataSourceProperties;
    private TaskConfigurationProperties myProp;

    InfoController(final DataSourceProperties dataSourceProperties, final TaskConfigurationProperties myProp) {
        this.dataSourceProperties = dataSourceProperties;
        this.myProp = myProp;
    }

    @GetMapping("/url")
    String url() {
        return dataSourceProperties.getUrl();
    }

    @GetMapping("/prop")
    boolean myProp() {
        return myProp.getTemplate().isAllowMultipleTasks();
    }
}
