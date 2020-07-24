package com.example.januush.todolistapp.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();

    Page<Task> findAll(Pageable page);

    Optional<Task> findById(Integer id); //TODO may not find any task exception

    boolean existsById(Integer id);

    boolean existsByDoneIsFalseAndGroupId(Integer groupId);

    Task save(Task entity);

    List<Task> findByDone(boolean done);

    List<Task> findAllByGroup_Id(Integer groupId);
}
