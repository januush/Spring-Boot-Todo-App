package com.example.januush.todolistapp;

import com.example.januush.todolistapp.model.Task;
import com.example.januush.todolistapp.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

@Configuration
class TestConfiguration {
	@Bean
	@Primary
	@Profile("integration")
	TaskRepository testRepo() {
		return new TaskRepository() {
			private int index = 0;
			private Map<Integer, Task> tasks = new HashMap<>();
			@Override
			public List<Task> findAll() {
				return new ArrayList<>(tasks.values());
			}

			@Override
			public Page<Task> findAll(final Pageable page) {
				return null;
			}

			@Override
			public Optional<Task> findById(final Integer id) {
				return Optional.ofNullable(tasks.get(id));
			}

			@Override
			public boolean existsById(final Integer id) {
				return false;
			}

			@Override
			public boolean existsByDoneIsFalseAndGroupId(final Integer groupId) {
				return false;
			}

			@Override
			public Task save(final Task entity) {
				if(entity.getId() != 0) {
					tasks.put(entity.getId(), entity);
				} else {
					tasks.put(++index, entity);
				}
				return entity;
			}

			@Override
			public List<Task> findByDone(final boolean done) {
				return null;
			}
		};
	}
}
