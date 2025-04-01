package com.example.taskmanager.controllers;

import com.example.taskmanager.schemas.ProjectDTO;
import com.example.taskmanager.schemas.ProjectTaskDTO;
import com.example.taskmanager.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create a new project", description = "Method to create a new project in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created project"),
            @ApiResponse(responseCode = "400", description = "Invalid project data")
    })
    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDto) {
        ProjectDTO createdProject = projectService.createProject(projectDto);
        return ResponseEntity.ok(createdProject);
    }

    @Operation(summary = "Get project task information",
            description = "Method to get detailed task information for projects")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved task information"),
            @ApiResponse(responseCode = "404", description = "No tasks found")
    })
    @GetMapping("/info")
    public ResponseEntity<List<ProjectTaskDTO>> getInfo() {
        List<ProjectTaskDTO> projectTaskList = projectService.getAllInfo();
        return ResponseEntity.ok(projectTaskList);
    }

    @Operation(summary = "Get all projects", description = "Method to retrieve all projects")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of projects"),
            @ApiResponse(responseCode = "404", description = "No projects found")
    })
    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @Operation(summary = "Get project by ID", description = "Method to retrieve a specific project by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved project"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        Optional<ProjectDTO> project = projectService.getProjectById(id);
        return project.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update project by ID", description = "Method to update project details by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated project"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id,
                                                    @RequestBody ProjectDTO projectDto) {
        Optional<ProjectDTO> updatedProject = projectService.updateProject(id, projectDto);
        return updatedProject.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete project by ID", description = "Method to delete a project by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted project"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        boolean isDeleted = projectService.deleteProject(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
