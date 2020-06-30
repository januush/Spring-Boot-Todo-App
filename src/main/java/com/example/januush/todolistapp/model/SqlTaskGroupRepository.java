package com.example.januush.todolistapp.model;

import org.springframework.data.jpa.repository.JpaRepository;

interface SqlTaskGroupRepository extends JpaRepository<TaskGroup, Integer>, TaskGroupRepository {
}
