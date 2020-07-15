package com.example.januush.todolistapp.controller;

import com.example.januush.todolistapp.model.Task;
import com.example.januush.todolistapp.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("integration")// instead of prod db, use test class
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
		repo.save(new Task("foo", LocalDateTime.now()));
		repo.save(new Task("bar", LocalDateTime.now()));
		// when
		Task[] result = restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);
		// then
		assertThat(result).hasSize(2);
	}
}