package com.example.taskmanager.repository;

import com.example.taskmanager.models.Project;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query(value = "SELECT p.* FROM projects p " +
            "JOIN tasks t ON p.id = t.project_id " +
            "WHERE t.name = :taskName", nativeQuery = true)
    List<Project> findAllWithTasks(@Param("taskName") String taskName);
}
