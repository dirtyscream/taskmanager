package com.example.taskmanager.repository;

import com.example.taskmanager.models.Task;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);

    Optional<Task> findByIdAndProjectId(Long taskId, Long projectId);
}
