package com.example.taskmanager.service;

import com.example.taskmanager.cache.TaskCache;
import com.example.taskmanager.exceptions.TaskNotFoundException;
import com.example.taskmanager.models.Project;
import com.example.taskmanager.models.Task;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.schemas.TaskDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskCache taskCache;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskCache taskCache) {
        this.taskRepository = taskRepository;
        this.taskCache = taskCache;
    }

    public TaskDTO createTask(TaskDTO taskDto, Long projectId) {
        Project project = new Project();
        project.setId(projectId);
        Task task = taskDto.toEntity();
        task.setProject(project);
        Task savedTask = taskRepository.save(task);
        taskCache.invalidate(projectId);
        return TaskDTO.fromEntity(savedTask);
    }

    public List<TaskDTO> getAllTasks(Long projectId) {
        List<Task> tasks = taskCache.getTasks(projectId);
        if (tasks == null) {
            tasks = taskRepository.findByProjectId(projectId);
            if (tasks.isEmpty()) {
                throw new TaskNotFoundException(projectId);
            }
            taskCache.putTasks(projectId, tasks);
        }
        return tasks.stream().map(TaskDTO::fromEntity).toList();
    }

    public Optional<TaskDTO> getTaskById(Long taskId, Long projectId) {
        return taskRepository.findByIdAndProjectId(taskId, projectId)
                .map(TaskDTO::fromEntity)
                .or(() -> {
                    throw new TaskNotFoundException(taskId);
                });
    }

    public Optional<TaskDTO> updateTask(Long taskId, Long projectId, TaskDTO taskDto) {
        return taskRepository.findByIdAndProjectId(taskId, projectId).map(existingTask -> {
            Task task = taskDto.toEntity();
            task.setId(taskId);
            task.setProject(existingTask.getProject());
            Task updatedTask = taskRepository.save(task);
            taskCache.invalidate(projectId);
            return Optional.of(TaskDTO.fromEntity(updatedTask));
        }).orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    public boolean deleteTask(Long id) {
        return taskRepository.findById(id).map(task -> {
            taskRepository.deleteById(id);
            taskCache.invalidate(task.getProject().getId());
            return true;
        }).orElseThrow(() -> new TaskNotFoundException(id));
    }
}

