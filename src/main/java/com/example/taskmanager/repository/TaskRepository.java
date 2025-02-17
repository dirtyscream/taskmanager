package com.example.taskmanager.repository;

import com.example.taskmanager.models.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel, Long> {
    List<TaskModel> findByProjectId(Long projectId);
    Optional<TaskModel> findByIdAndProjectId(Long taskId, Long projectId);
}
