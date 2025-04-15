package com.example.taskmanager.exceptions;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(Long id) {
        super("Project with ID " + id + " not found");
    }
}

