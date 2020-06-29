package com.example.januush.todolistapp.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {
    // override existing method with custom sql
    @Override
    @Query(nativeQuery = true, value = "SELECT COUNT(*) > 0 FROM tasks WHERE id=:id")
    boolean existsById(@Param("id") Integer id);
}
