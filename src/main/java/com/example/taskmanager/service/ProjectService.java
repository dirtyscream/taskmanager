package com.example.taskmanager.service;

import com.example.taskmanager.models.ProjectModel;
import com.example.taskmanager.repository.ProjectRepository;
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

    public ProjectModel createProject(ProjectModel projectModel) {
        return projectRepository.save(projectModel);
    }

    public List<ProjectModel> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<ProjectModel> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public ProjectModel updateProject(Long id, ProjectModel projectModel) {
        if (projectRepository.existsById(id)) {
            projectModel.setId(id);
            return projectRepository.save(projectModel);
        }
        return null;
    }

    public boolean deleteProject(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
