package com.example.taskmanager.service;

import com.example.taskmanager.models.Project;
import com.example.taskmanager.models.Task;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.schemas.TaskDTO;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final Map<Long, List<Task>> cache = new ConcurrentHashMap<>();

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskDTO createTask(TaskDTO taskDto, Long projectId) {
        Project project = new Project();
        project.setId(projectId);

        Task task = taskDto.toEntity();
        task.setProject(project);

        Task savedTask = taskRepository.save(task);
        return TaskDTO.fromEntity(savedTask);
    }

    public List<TaskDTO> getAllTasks(Long projectId) {
        return cache.computeIfAbsent(projectId, id -> taskRepository.findByProjectId(projectId))
                .stream()
                .map(TaskDTO::fromEntity)
                .toList();
    }

    public Optional<TaskDTO> getTaskById(Long taskId, Long projectId) {
        Optional<Task> task = taskRepository.findByIdAndProjectId(taskId, projectId);
        return task.map(TaskDTO::fromEntity);
    }

    public Optional<TaskDTO> updateTask(Long taskId, Long projectId, TaskDTO taskDto) {
        if (taskRepository.existsById(taskId)) {
            Task task = taskDto.toEntity();
            Project project = new Project();
            project.setId(projectId);
            task.setId(taskId);
            task.setProject(project);
            Task updatedTask = taskRepository.save(task);
            return Optional.of(TaskDTO.fromEntity(updatedTask));
        }
        return Optional.empty();
    }

    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
