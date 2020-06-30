package com.example.januush.todolistapp.adapter;

import com.example.januush.todolistapp.model.TaskGroup;
import com.example.januush.todolistapp.model.TaskGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;

interface SqlTaskGroupRepository extends JpaRepository<TaskGroup, Integer>, TaskGroupRepository {
}
