package com.example.taskmanager.service;

import com.example.taskmanager.exceptions.NotFoundException;
import com.example.taskmanager.models.Project;
import com.example.taskmanager.models.Task;
import com.example.taskmanager.repository.ProjectRepository;
import com.example.taskmanager.schemas.ProjectDTO;
import com.example.taskmanager.schemas.ProjectTaskDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project project;
    private ProjectDTO projectDTO;

    @BeforeEach
    void setUp() {
        project = Project.builder()
                .id(1L)
                .name("Test Project")
                .description("Test Description")
                .build();

        projectDTO = new ProjectDTO(1L, "Test Project", "Test Description");
    }

    @Test
    void createProject_ShouldReturnProjectDTO() {
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        ProjectDTO result = projectService.createProject(projectDTO);
        assertNotNull(result);
        assertEquals(projectDTO.getName(), result.getName());
        assertEquals(projectDTO.getDescription(), result.getDescription());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void createProject_WithNullDescription_ShouldReturnProjectDTO() {
        ProjectDTO noDescDTO = new ProjectDTO(null, "Test Project", null);
        Project noDescProject = Project.builder()
                .id(1L)
                .name("Test Project")
                .description(null)
                .build();

        when(projectRepository.save(any(Project.class))).thenReturn(noDescProject);

        ProjectDTO result = projectService.createProject(noDescDTO);

        assertNotNull(result);
        assertEquals(noDescDTO.getName(), result.getName());
        assertNull(result.getDescription());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void getAllProjects_WhenNoProjects_ShouldReturnEmptyList() {
        when(projectRepository.findAll()).thenReturn(Collections.emptyList());

        List<ProjectDTO> result = projectService.getAllProjects();

        assertTrue(result.isEmpty());
        verify(projectRepository).findAll();
    }

    @Test
    void getAllProjects_ShouldReturnListOfProjectDTOs() {
        when(projectRepository.findAll()).thenReturn(Collections.singletonList(project));

        List<ProjectDTO> result = projectService.getAllProjects();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(projectDTO.getName(), result.get(0).getName());
        verify(projectRepository).findAll();
    }

    @Test
    void getProjectById_WhenProjectExists_ShouldReturnProjectDTO() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Optional<ProjectDTO> result = projectService.getProjectById(1L);

        assertTrue(result.isPresent());
        assertEquals(projectDTO.getName(), result.get().getName());
        verify(projectRepository).findById(1L);
    }

    @Test
    void getProjectById_WhenProjectNotExists_ShouldThrowNotFoundException() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> projectService.getProjectById(1L));
        verify(projectRepository).findById(1L);
    }

    @Test
    void updateProject_WhenProjectExists_ShouldReturnUpdatedProjectDTO() {
        ProjectDTO updatedDTO = new ProjectDTO(null, "Updated Project", "Updated Description");
        Project updatedProject = Project.builder()
                .id(1L)
                .name("Updated Project")
                .description("Updated Description")
                .build();

        when(projectRepository.existsById(1L)).thenReturn(true);
        when(projectRepository.save(any(Project.class))).thenReturn(updatedProject);

        Optional<ProjectDTO> result = projectService.updateProject(1L, updatedDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Project", result.get().getName());
        assertEquals("Updated Description", result.get().getDescription());
        verify(projectRepository).existsById(1L);
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void updateProject_WhenProjectNotExists_ShouldThrowNotFoundException() {
        ProjectDTO updatedDTO = new ProjectDTO(null, "Updated Project", "Updated Description");
        when(projectRepository.existsById(1L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> projectService.updateProject(1L, updatedDTO));
        verify(projectRepository).existsById(1L);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void deleteProject_WhenProjectExists_ShouldReturnTrue() {
        when(projectRepository.existsById(1L)).thenReturn(true);
        doNothing().when(projectRepository).deleteById(1L);

        boolean result = projectService.deleteProject(1L);

        assertTrue(result);
        verify(projectRepository).existsById(1L);
        verify(projectRepository).deleteById(1L);
    }

    @Test
    void deleteProject_WhenProjectNotExists_ShouldReturnFalse() {
        when(projectRepository.existsById(1L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> projectService.deleteProject(1L));
        verify(projectRepository).existsById(1L);
        verify(projectRepository, never()).deleteById(1L);
    }

    @Test
    void getAllInfo_WhenNoProjects_ShouldReturnEmptyList() {
        when(projectRepository.findAllWithTasks("Test Task")).thenReturn(Collections.emptyList());

        List<ProjectTaskDTO> result = projectService.getAllInfo("Test Task");

        assertTrue(result.isEmpty());
        verify(projectRepository).findAllWithTasks("Test Task");
    }

    @Test
    void getAllInfo_WhenProjectHasNoTasks_ShouldReturnProjectWithEmptyTaskList() {
        Project projectWithoutTasks = Project.builder()
                .id(1L)
                .name("Test Project")
                .description("Test Description")
                .tasks(Collections.emptyList())
                .build();

        when(projectRepository.findAllWithTasks("Non-existent Task")).thenReturn(Collections.singletonList(projectWithoutTasks));

        List<ProjectTaskDTO> result = projectService.getAllInfo("Non-existent Task");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test Project", result.get(0).name());
        assertTrue(result.get(0).tasks().isEmpty());
        verify(projectRepository).findAllWithTasks("Non-existent Task");
    }

    @Test
    void getAllInfo_ShouldReturnProjectTaskDTOs() {
        Project projectWithTasks = Project.builder()
                .id(1L)
                .name("Test Project")
                .description("Test Description")
                .tasks(Collections.singletonList(
                        Task.builder()
                                .id(1L)
                                .name("Test Task")
                                .info("Test Info")
                                .deadline(LocalDateTime.now().plusDays(1))
                                .build()
                ))
                .build();

        when(projectRepository.findAllWithTasks("Test Task")).thenReturn(Collections.singletonList(projectWithTasks));

        List<ProjectTaskDTO> result = projectService.getAllInfo("Test Task");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test Project", result.get(0).name());
        assertEquals(1, result.get(0).tasks().size());
        assertEquals("Test Task", result.get(0).tasks().get(0).getName());
        verify(projectRepository).findAllWithTasks("Test Task");
    }
}