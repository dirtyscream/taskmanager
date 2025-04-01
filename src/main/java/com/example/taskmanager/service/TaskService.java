package com.example.taskmanager.service;

import com.example.taskmanager.models.Project;
import com.example.taskmanager.models.Task;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.schemas.TaskDTO;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final Map<Long, List<Task>> cache;

    private static final long MAX_CACHE_SIZE = 1_000_000_000L; // 1GB
    private long currentCacheSize = 0;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.cache = Collections.synchronizedMap(new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, List<Task>> eldest) {
                if (currentCacheSize > MAX_CACHE_SIZE) {
                    currentCacheSize -= estimateSize(eldest.getValue());
                    return true;
                }
                return false;
            }
        });
    }

    public TaskDTO createTask(TaskDTO taskDto, Long projectId) {
        Project project = new Project();
        project.setId(projectId);
        Task task = taskDto.toEntity();
        task.setProject(project);
        Task savedTask = taskRepository.save(task);
        invalidateCache(projectId);
        return TaskDTO.fromEntity(savedTask);
    }

    public List<TaskDTO> getAllTasks(Long projectId) {
        List<Task> tasks = cache.computeIfAbsent(projectId, id -> {
            List<Task> fetchedTasks = taskRepository.findByProjectId(projectId);
            currentCacheSize += estimateSize(fetchedTasks);
            return fetchedTasks;
        });
        return tasks.stream().map(TaskDTO::fromEntity).toList();
    }

    public Optional<TaskDTO> getTaskById(Long taskId, Long projectId) {
        return taskRepository.findByIdAndProjectId(taskId, projectId).map(TaskDTO::fromEntity);
    }

    public Optional<TaskDTO> updateTask(Long taskId, Long projectId, TaskDTO taskDto) {
        Optional<Task> existingTask = taskRepository.findByIdAndProjectId(taskId, projectId);
        if (existingTask.isPresent()) {
            Task task = taskDto.toEntity();
            task.setId(taskId);
            task.setProject(existingTask.get().getProject());

            Task updatedTask = taskRepository.save(task);
            invalidateCache(projectId);
            return Optional.of(TaskDTO.fromEntity(updatedTask));
        }
        return Optional.empty();
    }

    public boolean deleteTask(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Long projectId = optionalTask.get().getProject().getId();
            taskRepository.deleteById(id);
            invalidateCache(projectId);
            return true;
        }
        return false;
    }

    private void invalidateCache(Long projectId) {
        List<Task> removed = cache.remove(projectId);
        if (removed != null) {
            currentCacheSize -= estimateSize(removed);
        }
    }

    private long estimateSize(List<Task> tasks) {
        return tasks.size() * 200L;
    }
}
