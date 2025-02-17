package com.example.taskmanager.service;

import com.example.taskmanager.models.ProjectModel;
import com.example.taskmanager.models.TaskModel;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskModel createTask(TaskModel taskModel, Long projectId) {
        ProjectModel project = new ProjectModel();
        project.setId(projectId);
        taskModel.setProject(project);
        return taskRepository.save(taskModel);
    }

    public List<TaskModel> getAllTasks(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public Optional<TaskModel> getTaskById(Long taskId, Long projectId) {
        return taskRepository.findByIdAndProjectId(taskId, projectId);
    }

    public TaskModel updateTask(Long id, TaskModel taskModel) {
        if (taskRepository.existsById(id)) {
            taskModel.setId(id);
            return taskRepository.save(taskModel);
        }
        return null;
    }

    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
