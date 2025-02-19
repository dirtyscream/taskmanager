package com.example.taskmanager.controllers;

import com.example.taskmanager.models.TaskModel;
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
    public ResponseEntity<TaskModel> createTask(@RequestBody TaskModel taskModel,
                                                @PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.createTask(taskModel, projectId));
    }

    @GetMapping
    public ResponseEntity<List<TaskModel>> getAllTasks(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getAllTasks(projectId));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskModel> getTaskById(@PathVariable Long taskId, @PathVariable Long projectId) {
        Optional<TaskModel> task = taskService.getTaskById(taskId, projectId);
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskModel> updateTask(@PathVariable Long taskId, @RequestBody TaskModel taskModel) {
        TaskModel updatedTask = taskService.updateTask(taskId, taskModel);
        if (updatedTask != null) {
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
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
