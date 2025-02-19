package com.example.taskmanager.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FirstSchema {
    @JsonProperty("name")
    private String name;

    @JsonProperty("amountTasks")
    private Integer amountTasks;

    public FirstSchema(String name, Integer amountTasks) {
        this.name = name;
        this.amountTasks = amountTasks;
    }
}