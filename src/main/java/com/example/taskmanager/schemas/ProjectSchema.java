package com.example.taskmanager.schemas;

import com.example.taskmanager.models.Project;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectSchema {
    private final Long id;
    private final String name;
    private final String description;

    public ProjectSchema(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public static ProjectSchema fromEntity(Project project) {
        return new ProjectSchema(
                project.getId(),
                project.getName(),
                project.getDescription()
        );
    }

    public Project toEntity() {
        return Project.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .build();
    }
}
