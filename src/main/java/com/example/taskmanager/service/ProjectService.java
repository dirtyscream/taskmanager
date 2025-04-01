package com.example.taskmanager.service;

import com.example.taskmanager.models.Project;
import com.example.taskmanager.repository.ProjectRepository;
import com.example.taskmanager.schemas.ProjectDTO;
import com.example.taskmanager.schemas.ProjectTaskDTO;
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
                .map(ProjectDTO::fromEntity);
    }

    public Optional<ProjectDTO> updateProject(Long id, ProjectDTO projectDto) {
        if (projectRepository.existsById(id)) {
            Project updatedProject = projectDto.toEntity();
            updatedProject.setId(id);
            return Optional.of(ProjectDTO.fromEntity(projectRepository.save(updatedProject)));
        }
        return Optional.empty();
    }

    public boolean deleteProject(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<ProjectTaskDTO> getAllInfo() {
        List<Project> projects = projectRepository.findAllWithTasks();
        return projects.stream()
                .map(ProjectTaskDTO::fromEntity)
                .toList();
    }
}
