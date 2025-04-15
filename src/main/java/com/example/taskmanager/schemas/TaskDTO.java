package com.example.taskmanager.schemas;

import com.example.taskmanager.models.Task;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskDTO {
    private final Long id;

    @NotBlank(message = "Task name is required")
    @Size(max = 255, message = "Task name must be at most 255 characters")
    private final String name;

    @Size(max = 1000, message = "Info must be at most 1000 characters")
    private final String info;

    @Future(message = "Deadline must be in the future")
    private final LocalDateTime deadline;

    public TaskDTO(Long id, String name, String info, LocalDateTime deadline) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.deadline = deadline;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("info")
    public String getInfo() {
        return info;
    }

    @JsonProperty("deadline")
    public LocalDateTime getDeadline() {
        return deadline;
    }

    public static TaskDTO fromEntity(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getName(),
                task.getInfo(),
                task.getDeadline()
        );
    }

    public Task toEntity() {
        return Task.builder()
                .id(this.id)
                .name(this.name)
                .info(this.info)
                .deadline(this.deadline)
                .build();
    }
}
