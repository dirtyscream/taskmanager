package com.example.taskmanager.repository;

import com.example.taskmanager.models.Project;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.tasks")
    List<Project> findAllWithTasks();
}
