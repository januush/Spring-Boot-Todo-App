package com.example.januush.todolistapp;

import com.example.januush.todolistapp.model.Task;
import com.example.januush.todolistapp.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.*;

@Configuration
class TestConfiguration {
	@Bean
	@Primary
	@Profile("!integration")
	DataSource e2eTestDataSource() {
		var result = new DriverManagerDataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
		result.setDriverClassName("org.h2.Driver");
		return result;
	}

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
				int key = tasks.size() + 1;
				try {
					var field = Task.class.getDeclaredField("id");
					field.setAccessible(true);
					field.set(entity, key); // set id with reflection
				} catch (NoSuchFieldException | IllegalAccessException e) {
					throw new RuntimeException();
				}
				tasks.put(key, entity);
				return tasks.get(key);
			}

			@Override
			public List<Task> findByDone(final boolean done) {
				return null;
			}
		};
	}
}
