package com.example.januush.todolistapp.controller;

import com.example.januush.todolistapp.model.Task;
import com.example.januush.todolistapp.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTestE2ETest {
	@LocalServerPort
	private int port; // port number

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	TaskRepository repo;

	@Test
	void httpGet_returnsAllTasks() {
		// given
        int initial = repo.findAll().size();
        repo.save(new Task("foo", LocalDateTime.now()));
		repo.save(new Task("bar", LocalDateTime.now()));
		// when
		Task[] result = restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);
		// then
		assertThat(result).hasSize(initial + 2);
	}
}