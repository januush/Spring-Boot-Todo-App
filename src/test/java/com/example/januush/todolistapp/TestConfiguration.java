package com.example.januush.todolistapp;

import com.example.januush.todolistapp.model.Task;
import com.example.januush.todolistapp.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Configuration
class TestConfiguration {
    @Bean
    @Profile("integration")
    TaskRepository testRepo() {
        return new TaskRepository() {
            @Override
            public List<Task> findAll() {
                return null;
            }

            @Override
            public Page<Task> findAll(final Pageable page) {
                return null;
            }

            @Override
            public Optional<Task> findById(final Integer id) {
                return Optional.empty();
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
                return null;
            }

            @Override
            public List<Task> findByDone(final boolean done) {
                return null;
            }
        };
    }
}
