package com.example.taskmanager.service;

import com.example.taskmanager.models.Project;
import com.example.taskmanager.models.Task;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.schemas.TaskSchema;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskSchema createTask(TaskSchema taskSchema, Long projectId) {
        Project project = new Project();
        project.setId(projectId);

        Task task = taskSchema.toEntity();
        task.setProject(project);

        Task savedTask = taskRepository.save(task);
        return TaskSchema.fromEntity(savedTask);
    }

    public List<TaskSchema> getAllTasks(Long projectId) {
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        return tasks.stream()
                .map(TaskSchema::fromEntity)
                .toList();
    }

    public Optional<TaskSchema> getTaskById(Long taskId, Long projectId) {
        Optional<Task> task = taskRepository.findByIdAndProjectId(taskId, projectId);
        return task.map(TaskSchema::fromEntity);
    }

    public Optional<TaskSchema> updateTask(Long taskId, Long projectId, TaskSchema taskSchema) {
        if (taskRepository.existsById(taskId)) {
            Task task = taskSchema.toEntity();
            Project project = new Project();
            project.setId(projectId);
            task.setId(taskId);
            task.setProject(project);
            Task updatedTask = taskRepository.save(task);
            return Optional.of(TaskSchema.fromEntity(updatedTask));
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
