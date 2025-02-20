package com.example.taskmanager.controllers;

import com.example.taskmanager.schemas.ProjectSchema;
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
    public ResponseEntity<ProjectSchema> createProject(@RequestBody ProjectSchema projectSchema) {
        ProjectSchema createdProject = projectService.createProject(projectSchema);
        return ResponseEntity.ok(createdProject);
    }

    @GetMapping
    public ResponseEntity<List<ProjectSchema>> getAllProjects() {
        List<ProjectSchema> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectSchema> getProjectById(@PathVariable Long id) {
        Optional<ProjectSchema> project = projectService.getProjectById(id);
        return project.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectSchema> updateProject(@PathVariable Long id,
                                                       @RequestBody ProjectSchema projectSchema) {
        Optional<ProjectSchema> updatedProject = projectService.updateProject(id, projectSchema);
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
