package com.example.taskmanager.service;

import com.example.taskmanager.cache.TaskCache;
import com.example.taskmanager.models.Project;
import com.example.taskmanager.models.Task;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.schemas.TaskDTO;
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
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskCache taskCache;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private TaskDTO taskDTO;
    private Project project;

    @BeforeEach
    void setUp() {
        project = Project.builder()
                .id(1L)
                .name("Test Project")
                .build();

        task = Task.builder()
                .id(1L)
                .name("Test Task")
                .info("Test Info")
                .deadline(LocalDateTime.now().plusDays(1))
                .project(project)
                .build();

        taskDTO = new TaskDTO(1L, "Test Task", "Test Info", LocalDateTime.now().plusDays(1));
    }

    @Test
    void createTask_ShouldReturnTaskDTOAndInvalidateCache() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO result = taskService.createTask(taskDTO, 1L);

        assertNotNull(result);
        assertEquals(taskDTO.getName(), result.getName());
        assertEquals(taskDTO.getInfo(), result.getInfo());
        verify(taskRepository).save(any(Task.class));
        verify(taskCache).invalidate(1L);
    }

    @Test
    void createTask_WithNullInfo_ShouldReturnTaskDTO() {
        TaskDTO noInfoDTO = new TaskDTO(null, "Test Task", null, LocalDateTime.now().plusDays(1));
        Task noInfoTask = Task.builder()
                .id(1L)
                .name("Test Task")
                .info(null)
                .deadline(LocalDateTime.now().plusDays(1))
                .project(project)
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(noInfoTask);

        TaskDTO result = taskService.createTask(noInfoDTO, 1L);

        assertNotNull(result);
        assertEquals(noInfoDTO.getName(), result.getName());
        assertNull(result.getInfo());
        verify(taskRepository).save(any(Task.class));
        verify(taskCache).invalidate(1L);
    }

    @Test
    void getAllTasks_WhenTasksInCache_ShouldReturnTasksFromCache() {
        List<Task> cachedTasks = Collections.singletonList(task);
        when(taskCache.getTasks(1L)).thenReturn(cachedTasks);

        List<TaskDTO> result = taskService.getAllTasks(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(taskDTO.getName(), result.get(0).getName());
        verify(taskCache).getTasks(1L);
        verify(taskRepository, never()).findByProjectId(1L);
        verify(taskCache, never()).putTasks(anyLong(), anyList());
    }

    @Test
    void getAllTasks_WhenTasksNotInCache_ShouldReturnTasksFromRepository() {
        when(taskCache.getTasks(1L)).thenReturn(null);
        when(taskRepository.findByProjectId(1L)).thenReturn(Collections.singletonList(task));

        List<TaskDTO> result = taskService.getAllTasks(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(taskDTO.getName(), result.get(0).getName());
        verify(taskCache).getTasks(1L);
        verify(taskRepository).findByProjectId(1L);
        verify(taskCache).putTasks(1L, Collections.singletonList(task));
    }

    @Test
    void getTaskById_WhenTaskExists_ShouldReturnTaskDTO() {
        when(taskRepository.findByIdAndProjectId(1L, 1L)).thenReturn(Optional.of(task));

        Optional<TaskDTO> result = taskService.getTaskById(1L, 1L);

        assertTrue(result.isPresent());
        assertEquals(taskDTO.getName(), result.get().getName());
        verify(taskRepository).findByIdAndProjectId(1L, 1L);
    }

    @Test
    void updateTask_WhenTaskExists_ShouldReturnUpdatedTaskDTO() {
        TaskDTO updatedDTO = new TaskDTO(null, "Updated Task", "Updated Info", LocalDateTime.now().plusDays(2));
        Task updatedTask = Task.builder()
                .id(1L)
                .name("Updated Task")
                .info("Updated Info")
                .deadline(LocalDateTime.now().plusDays(2))
                .project(project)
                .build();

        when(taskRepository.findByIdAndProjectId(1L, 1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Optional<TaskDTO> result = taskService.updateTask(1L, 1L, updatedDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Task", result.get().getName());
        assertEquals("Updated Info", result.get().getInfo());
        verify(taskRepository).findByIdAndProjectId(1L, 1L);
        verify(taskRepository).save(any(Task.class));
        verify(taskCache).invalidate(1L);
    }


    @Test
    void deleteTask_WhenTaskExists_ShouldReturnTrueAndInvalidateCache() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).deleteById(1L);

        boolean result = taskService.deleteTask(1L);

        assertTrue(result);
        verify(taskRepository).findById(1L);
        verify(taskRepository).deleteById(1L);
        verify(taskCache).invalidate(1L);
    }

}