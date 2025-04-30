package com.example.taskmanager.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    private final Map<UUID, TaskStatus> taskStatusMap = new ConcurrentHashMap<>();
    private final Map<UUID, String> completedTasks = new ConcurrentHashMap<>();

    @Async
    public CompletableFuture<UUID> initiateLogCollection(LocalDate date) {
        UUID taskId = UUID.randomUUID();
        taskStatusMap.put(taskId, new TaskStatus(taskId, Status.IN_PROGRESS, "Log collection started"));
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                String result = "Logs collected for date: " + date.toString();
                completedTasks.put(taskId, result);
                taskStatusMap.put(taskId, new TaskStatus(taskId, Status.COMPLETED,
                        "Log collection completed"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                taskStatusMap.put(taskId, new TaskStatus(taskId, Status.FAILED,
                        "Log collection failed: " + e.getMessage()));
            }
        }).start();

        return CompletableFuture.completedFuture(taskId);
    }

    public TaskStatus getTaskStatus(UUID taskId) {
        return taskStatusMap.getOrDefault(taskId,
                new TaskStatus(taskId, Status.NOT_FOUND, "Task not found"));
    }

    @Async
    public CompletableFuture<Resource> getLogFileByDateAsync(LocalDate date) {
        try {
            Thread.sleep(15000);

            Map<UUID, String> logsForDate = completedTasks.entrySet().stream()
                    .filter(entry -> {
                        TaskStatus status = taskStatusMap.get(entry.getKey());
                        return status != null &&
                                status.getStatus() == Status.COMPLETED &&
                                status.getMessage().contains(date.toString());
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            if (logsForDate.isEmpty()) {
                return CompletableFuture.completedFuture(null);
            }
            String allLogs = String.join("\n", logsForDate.values());
            ByteArrayResource resource = new ByteArrayResource(allLogs.getBytes(StandardCharsets.UTF_8));

            return CompletableFuture.completedFuture(resource);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            return CompletableFuture.completedFuture(null);
        }
    }

    public enum Status {
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        NOT_FOUND
    }

    @Data
    @AllArgsConstructor
    public static class TaskStatus {
        private UUID taskId;
        private Status status;
        private String message;
    }
}