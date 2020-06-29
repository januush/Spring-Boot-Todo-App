package com.example.januush.todolistapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.*;


@RestController
class InfoController {
    @Autowired
    private DataSourceProperties dataSourceProperties;
    @Value("${my.prop}")
    private String myProp;

    @GetMapping("/info/url")
    String url() {
        return dataSourceProperties.getUrl();
    }

    @GetMapping("/info/prop")
    String myProp() {
        return myProp;
    }
}
