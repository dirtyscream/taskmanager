package com.example.taskmanager.controllers;

import com.example.taskmanager.schemas.ProjectDTO;
import com.example.taskmanager.schemas.ProjectTaskDTO;
import com.example.taskmanager.service.ProjectService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDto) {
        ProjectDTO createdProject = projectService.createProject(projectDto);
        return ResponseEntity.ok(createdProject);
    }

    @GetMapping("/info")
    public ResponseEntity<List<ProjectTaskDTO>> getInfo() {
        List<ProjectTaskDTO> projectTaskList = projectService.getAllInfo();
        return ResponseEntity.ok(projectTaskList);
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        Optional<ProjectDTO> project = projectService.getProjectById(id);
        return project.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id,
                                                    @RequestBody ProjectDTO projectDto) {
        Optional<ProjectDTO> updatedProject = projectService.updateProject(id, projectDto);
        return updatedProject.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        boolean isDeleted = projectService.deleteProject(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
