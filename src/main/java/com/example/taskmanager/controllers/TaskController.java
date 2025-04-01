package com.example.taskmanager.controllers;

import com.example.taskmanager.schemas.TaskDTO;
import com.example.taskmanager.service.TaskService;
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
@RequestMapping("/api/projects/{projectId}/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Create a new task", description = "Method to create a new task for project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created task"),
            @ApiResponse(responseCode = "400", description = "Invalid task data")
    })
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDto,
                                              @PathVariable Long projectId) {
        TaskDTO createdTask = taskService.createTask(taskDto, projectId);
        return ResponseEntity.ok(createdTask);
    }

    @Operation(summary = "Get all tasks for a project", description = "Method to retrieve tasks for project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of tasks"),
            @ApiResponse(responseCode = "404", description = "No tasks found for the project")
    })
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(@PathVariable Long projectId) {
        List<TaskDTO> tasks = taskService.getAllTasks(projectId);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Get task by ID", description = "Method to retrieve a task by its ID for project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved task"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long taskId, @PathVariable Long projectId) {
        Optional<TaskDTO> task = taskService.getTaskById(taskId, projectId);
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update task by ID", description = "Method to update a task for a project by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated task"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long taskId, @PathVariable Long projectId,
                                              @RequestBody TaskDTO taskDto) {
        Optional<TaskDTO> updatedTask = taskService.updateTask(taskId, projectId, taskDto);
        return updatedTask.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete task by ID", description = "Method to delete a task for a project by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted task"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId, @PathVariable Long projectId) {
        boolean isDeleted = taskService.deleteTask(taskId);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
