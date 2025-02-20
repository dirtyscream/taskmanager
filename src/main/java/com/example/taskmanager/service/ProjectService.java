package com.example.taskmanager.service;

import com.example.taskmanager.models.Project;
import com.example.taskmanager.repository.ProjectRepository;
import com.example.taskmanager.schemas.ProjectSchema;
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

    public ProjectSchema createProject(ProjectSchema projectSchema) {
        Project project = projectSchema.toEntity();
        return ProjectSchema.fromEntity(projectRepository.save(project));
    }

    public List<ProjectSchema> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(ProjectSchema::fromEntity)
                .toList();
    }

    public Optional<ProjectSchema> getProjectById(Long id) {
        return projectRepository.findById(id)
                .map(ProjectSchema::fromEntity);
    }

    public Optional<ProjectSchema> updateProject(Long id, ProjectSchema projectSchema) {
        if (projectRepository.existsById(id)) {
            Project updatedProject = projectSchema.toEntity();
            updatedProject.setId(id);
            return Optional.of(ProjectSchema.fromEntity(projectRepository.save(updatedProject)));
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
}
