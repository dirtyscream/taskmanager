package com.example.taskmanager.controllers;

import com.example.taskmanager.schemas.TaskSchema;
import com.example.taskmanager.service.TaskService;
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

    @PostMapping()
    public ResponseEntity<TaskSchema> createTask(@RequestBody TaskSchema taskSchema,
                                                 @PathVariable Long projectId) {
        TaskSchema createdTask = taskService.createTask(taskSchema, projectId);
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping
    public ResponseEntity<List<TaskSchema>> getAllTasks(@PathVariable Long projectId) {
        List<TaskSchema> tasks = taskService.getAllTasks(projectId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskSchema> getTaskById(@PathVariable Long taskId, @PathVariable Long projectId) {
        Optional<TaskSchema> task = taskService.getTaskById(taskId, projectId);
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskSchema> updateTask(@PathVariable Long taskId, @PathVariable Long projectId,
                                                 @RequestBody TaskSchema taskSchema) {
        Optional<TaskSchema> updatedTask = taskService.updateTask(taskId, projectId, taskSchema);
        return updatedTask.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        boolean isDeleted = taskService.deleteTask(taskId);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
