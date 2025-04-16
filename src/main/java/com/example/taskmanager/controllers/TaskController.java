package com.example.taskmanager.controllers;

import com.example.taskmanager.schemas.TaskDTO;
import com.example.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
@Tag(name = "Задачи", description = "Управление задачами в проектах")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping()
    @Operation(summary = "Создать задачу в проекте")
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDto,
                                              @PathVariable Long projectId) {
        TaskDTO createdTask = taskService.createTask(taskDto, projectId);
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping
    @Operation(summary = "Получить все задачи проекта")
    public ResponseEntity<List<TaskDTO>> getAllTasks(@PathVariable Long projectId) {
        List<TaskDTO> tasks = taskService.getAllTasks(projectId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "Получить задачу по ID в рамках проекта")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long taskId, @PathVariable Long projectId) {
        Optional<TaskDTO> task = taskService.getTaskById(taskId, projectId);
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{taskId}")
    @Operation(summary = "Обновить задачу по ID в рамках проекта")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long taskId, @PathVariable Long projectId,
                                              @Valid @RequestBody TaskDTO taskDto) {
        Optional<TaskDTO> updatedTask = taskService.updateTask(taskId, projectId, taskDto);
        return updatedTask.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{taskId}")
    @Operation(summary = "Удалить задачу по ID в рамках проекта")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId, @PathVariable Long projectId) {
        boolean isDeleted = taskService.deleteTask(taskId);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
