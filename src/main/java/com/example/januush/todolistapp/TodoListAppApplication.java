package com.example.januush.todolistapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;

@SpringBootApplication
public class TodoListAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoListAppApplication.class, args);
	}

	@Bean
	Validator validator() {
		return new LocalValidatorFactoryBean();
	}
}
