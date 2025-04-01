package com.example.taskmanager.schemas;

import com.example.taskmanager.models.Project;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProjectTaskDTO(
        Long id,
        String name,
        String description,
        List<TaskDTO> tasks
) {
    public static ProjectTaskDTO fromEntity(Project project) {
        return new ProjectTaskDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getTasks().stream().map(TaskDTO::fromEntity).toList()
        );
    }
}
