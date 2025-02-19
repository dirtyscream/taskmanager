package com.example.taskmanager.repository;

import com.example.taskmanager.models.TaskModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel, Long> {
    List<TaskModel> findByProjectId(Long projectId);

    Optional<TaskModel> findByIdAndProjectId(Long taskId, Long projectId);
}
