package com.example.taskmanager.service;

import com.example.taskmanager.exceptions.NotFoundException;
import com.example.taskmanager.models.Project;
import com.example.taskmanager.repository.ProjectRepository;
import com.example.taskmanager.schemas.ProjectDTO;
import com.example.taskmanager.schemas.ProjectTaskDTO;
import com.example.taskmanager.schemas.TaskDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectDTO createProject(ProjectDTO projectDto) {
        Project project = projectDto.toEntity();
        return ProjectDTO.fromEntity(projectRepository.save(project));
    }

    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(ProjectDTO::fromEntity)
                .toList();
    }

    public Optional<ProjectDTO> getProjectById(Long id) {
        return projectRepository.findById(id)
                .map(ProjectDTO::fromEntity)
                .or(() -> {
                    throw new NotFoundException("Project not Found");
                });
    }

    public Optional<ProjectDTO> updateProject(Long id, ProjectDTO projectDto) {
        if (projectRepository.existsById(id)) {
            Project updatedProject = projectDto.toEntity();
            updatedProject.setId(id);
            return Optional.of(ProjectDTO.fromEntity(projectRepository.save(updatedProject)));
        }
        throw new NotFoundException("Project not Found");
    }

    public boolean deleteProject(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return true;
        }
        throw new NotFoundException("Project not Found");
    }

    public List<ProjectTaskDTO> getAllInfo(String taskName) {
        List<Project> projects = projectRepository.findAllWithTasks(taskName);
        return projects.stream()
                .map(project -> new ProjectTaskDTO(
                        project.getId(),
                        project.getName(),
                        project.getDescription(),
                        project.getTasks().stream()
                                .filter(task -> task.getName().equals(taskName))
                                .map(TaskDTO::fromEntity)
                                .toList()
                ))
                .toList();
    }
}

