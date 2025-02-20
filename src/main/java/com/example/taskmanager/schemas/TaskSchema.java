package com.example.taskmanager.schemas;

import com.example.taskmanager.models.Task;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)  // Не сериализовать null значения
public class TaskSchema {
    private final Long id;
    private final String name;
    private final String info;
    private final LocalDateTime deadline;

    public TaskSchema(Long id, String name, String info, LocalDateTime deadline) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.deadline = deadline;
    }

    @JsonProperty("id")  // Переименовать поле в JSON
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

    public static TaskSchema fromEntity(Task task) {
        return new TaskSchema(
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
